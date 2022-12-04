`include "mem_ctr.sv"

module cache #(
    parameter MEM_SIZE              = 2097152,
    parameter CACHE_SIZE            = 16384,
    parameter CACHE_LINE_SIZE       = 128,
    parameter CACHE_LINE_COUNT      = 128,
    parameter CACHE_WAY             = 2,
    parameter CACHE_SETS_COUNT      = 64,
    parameter CACHE_TAG_SIZE        = 8,
    parameter CACHE_SET_SIZE        = 6,
    parameter CACHE_OFFSET_SIZE     = 7,
    parameter CACHE_ADDR_SIZE       = 21,

    parameter DATA1_BUS_SIZE        = 16,
    parameter DATA2_BUS_SIZE        = 16,
    parameter ADDR1_BUS_SIZE        = 14,
    parameter ADDR2_BUS_SIZE        = 14,
    parameter CTR1_BUS_SIZE         = 3,
    parameter CTR2_BUS_SIZE         = 2,

    parameter C1_NOP                = 0,
    parameter C1_READ8              = 1,
    parameter C1_READ16             = 2,
    parameter C1_READ32             = 3,
    parameter C1_INVALIDATE_LINE    = 4,
    parameter C1_WRITE8             = 5,
    parameter C1_WRITE16            = 6,
    parameter C1_WRITE32            = 7,
    parameter C1_RESPONSE           = 7,
    parameter C2_NOP                = 0,
    parameter C2_READ_LINE          = 2,
    parameter C2_WRITE_LINE         = 3,
    parameter C2_RESPONSE           = 1
    ) (
    input                           CLK,
    input                           RESET,
    input wire[ADDR1_BUS_SIZE-1:0]  A1,
    inout wire[DATA1_BUS_SIZE-1:0]  D1,
    inout wire[CTR1_BUS_SIZE-1:0]   C1
    );
    output  wire[ADDR2_BUS_SIZE-1:0]    A2;
    inout   wire[DATA2_BUS_SIZE-1:0]    D2;
    inout   wire[CTR2_BUS_SIZE-1:0]     C2;

    mem_ctr ctr(CLK, RESET, A2, D2, C2);

    bit valid[CACHE_LINE_COUNT];
    bit dirty[CACHE_LINE_COUNT];
    reg[CACHE_TAG_SIZE-1:0] tag[CACHE_LINE_COUNT];
    reg[CACHE_LINE_SIZE-1:0] data[CACHE_LINE_COUNT];

    reg[CACHE_TAG_SIZE-1:0] addr_tag;
    reg[CACHE_SET_SIZE-1:0] addr_set;
    reg[CACHE_OFFSET_SIZE-1:0] addr_offset;

    byte cur_command;

    reg[DATA1_BUS_SIZE-1:0] d1;
    reg[DATA2_BUS_SIZE-1:0] d2;
    reg[ADDR2_BUS_SIZE-1:0] a2;
    reg[CTR1_BUS_SIZE-1:0]  c1;
    reg[CTR2_BUS_SIZE-1:0]  c2;

    bit C1_write_enabled = 0;
    bit D1_write_enabled = 0;
    bit command_is_running = 0;

    assign C1 = (C1_write_enabled) ? c1 : 'bz;
    assign D1 = (D1_write_enabled) ? d1 : 'bz;

    assign C2 = c2;
    assign D2 = d2;
    assign A2 = a2;

    int lru_priority[CACHE_LINE_COUNT];

    int cache_hits_count = 0;
    int cache_miss_count = 0;


    function cache_info();
        $display("Requets = %d", cache_miss_count + cache_hits_count);
        $display("Cache hits = %d", cache_hits_count);
        $display("Cache misses = %d", cache_miss_count);
        $display("Cache hit ratio = %f", (1.0 * cache_hits_count) / (1.0 * (cache_hits_count + cache_miss_count)));
    endfunction

    task reset;
        for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
            valid[i] = 0;
            dirty[i] = 0;
            tag[i] = 0;
            data[i] = 0;
        end
        addr_tag = 'bz;
        addr_set = 'bz;
        addr_offset = 'bz;
        cur_command = 0;
        command_is_running = 0;
        cache_hits_count = 0;
        cache_miss_count = 0;
    endtask

    task read_data_from_mem_by_addr(int addr);
        c2 = C2_READ_LINE;
        a2[7:0] = addr_tag;
        a2[13:8] = addr_set;
        d2 = 'bz;
        #200 valid[addr] = 1;
        tag[addr] = addr_tag;
        for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
            #2 data[addr][i +: DATA2_BUS_SIZE] = D2;
        end
    endtask

    task write_data_in_mem_by_addr(int addr);
        c2 = C2_WRITE_LINE;
        a2[7:0] = addr_tag;
        a2[13:8] = addr_set;
        for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
            #(i > 0 ? 2 : 0) d2 = data[addr][i +: DATA2_BUS_SIZE];
        end
        #2 d2 = 'bz;
        #198 dirty[addr] = 0;
    endtask

    task read_data_from_mem;
        #8// $display("Cache miss!");
        cache_miss_count++;
        if (valid[CACHE_WAY * addr_set] == 0) begin
            read_data_from_mem_by_addr(CACHE_WAY * addr_set);
        end
        else if (valid[CACHE_WAY * addr_set + 1] == 0) begin
            read_data_from_mem_by_addr(CACHE_WAY * addr_set + 1);
        end
        else if (lru_priority[CACHE_WAY * addr_set] < lru_priority[CACHE_WAY * addr_set + 2]) begin
            if (dirty[CACHE_WAY * addr_set] == 1) begin
                write_data_in_mem_by_addr(CACHE_WAY * addr_set);
            end
            read_data_from_mem_by_addr(CACHE_WAY * addr_set);
            lru_priority[CACHE_WAY * addr_set] = 1;
        end
        else begin
            if (dirty[CACHE_WAY * addr_set + 1] == 1) begin
                write_data_in_mem_by_addr(CACHE_WAY * addr_set + 1);
            end
            read_data_from_mem_by_addr(CACHE_WAY * addr_set + 1);
            lru_priority[CACHE_WAY * addr_set + 1] = 1;
        end
    endtask

    task read_data_from_cache(byte data_size);
        C1_write_enabled = 1;
        D1_write_enabled = 1;
        c1 = C1_RESPONSE;
        if (tag[CACHE_WAY * addr_set] == addr_tag) begin
            $display("Here");
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA2_BUS_SIZE) begin
                if (data_size < 16) begin
                    #(i > 0 ? 2 : 0) d1 = data[CACHE_WAY * addr_set][i +: 8];
                end
                else begin
                    #(i > 0 ? 2 : 0) d1 = data[CACHE_WAY * addr_set][i +: DATA2_BUS_SIZE];
                end
            end
            lru_priority[CACHE_WAY * addr_set]++;
        end
        else begin
            $display("there");
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA2_BUS_SIZE) begin
                if (data_size < 16) begin
                    #(i > 0 ? 2 : 0) d1 = data[CACHE_WAY * addr_set + 1][i +: 8];
                end
                else begin
                    #(i > 0 ? 2 : 0) d1 = data[CACHE_WAY * addr_set + 1][i +: DATA2_BUS_SIZE];
                end
            end
            lru_priority[CACHE_WAY * addr_set + 1]++;     
        end
    endtask

    task invaildate_line(int addr);
        if (dirty[addr] == 1) begin
            write_data_in_mem_by_addr(addr);
        end
        C1_write_enabled = 1;
        c1 = C1_RESPONSE;
        tag[addr] = 'bz;
        data[addr] = 0;
        dirty[addr] = 0;
        valid[addr] = 0;            
    endtask

    task write_data_in_cache(byte data_size);
        if (tag[CACHE_WAY * addr_set] == addr_tag) begin
            cache_hits_count++;
            C1_write_enabled = 1;
            c1 = C1_RESPONSE;
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA1_BUS_SIZE) begin
                if (data_size < DATA1_BUS_SIZE) begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +: 8] = D1;
                end
                else begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +: DATA1_BUS_SIZE] = D1;
                end
            end
            dirty[CACHE_WAY * addr_set] = 1;
        end
        else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
            cache_hits_count++;
            C1_write_enabled = 1;
            c1 = C1_RESPONSE;
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA1_BUS_SIZE) begin
                if (data_size < DATA1_BUS_SIZE) begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +: 8] = D1;
                end
                else begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +: DATA1_BUS_SIZE] = D1;
                end
            end
            dirty[CACHE_WAY * addr_set + 1] = 1;
        end
        else if (valid[CACHE_WAY * addr_set] == 0) begin
            cache_hits_count++;
            C1_write_enabled = 1;
            c1 = C1_RESPONSE;
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA1_BUS_SIZE) begin
                if (data_size < DATA1_BUS_SIZE) begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +: 8] = D1;
                end
                else begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +: DATA1_BUS_SIZE] = D1;
                end
            end
            dirty[CACHE_WAY * addr_set] = 1;
            valid[CACHE_WAY * addr_set] = 1;
        end
        else if (valid[CACHE_WAY * addr_set + 1] == 0) begin
            cache_hits_count++;
            C1_write_enabled = 1;
            c1 = C1_RESPONSE;
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA1_BUS_SIZE) begin
                if (data_size < DATA1_BUS_SIZE) begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +: 8] = D1;
                end
                else begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +: DATA1_BUS_SIZE] = D1;
                end
            end
            dirty[CACHE_WAY * addr_set + 1] = 1;
            valid[CACHE_WAY * addr_set + 1] = 1;
        end
        else if (lru_priority[CACHE_WAY * addr_set] < lru_priority[CACHE_WAY * addr_set + 1]) begin
            cache_miss_count++;
            if (dirty[CACHE_WAY * addr_set] == 1) begin
                write_data_in_mem_by_addr(CACHE_WAY * addr_set);
            end
            read_data_from_mem_by_addr(CACHE_WAY * addr_set);
            C1_write_enabled = 1;
            c1 = C1_RESPONSE;
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA1_BUS_SIZE) begin
                if (data_size < DATA1_BUS_SIZE) begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +: 8] = D1;
                end
                else begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +: DATA1_BUS_SIZE] = D1;
                end
            end

            dirty[CACHE_WAY * addr_set] = 1;
            valid[CACHE_WAY * addr_set] = 1;
            tag[CACHE_WAY * addr_set] = addr_tag;
        end
        else begin
            cache_miss_count++;
            if (dirty[CACHE_WAY * addr_set + 1] == 1) begin
                write_data_in_mem_by_addr(CACHE_WAY * addr_set + 1);
            end
            read_data_from_mem_by_addr(CACHE_WAY * addr_set + 1);
            C1_write_enabled = 1;
            c1 = C1_RESPONSE;
            for (int i = addr_offset; i < addr_offset + data_size; i += DATA1_BUS_SIZE) begin
                if (data_size < DATA1_BUS_SIZE) begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +: 8] = D1;
                end
                else begin
                    #(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +: DATA1_BUS_SIZE] = D1;
                end
            end
            dirty[CACHE_WAY * addr_set + 1] = 1;
            valid[CACHE_WAY * addr_set + 1] = 1;
            tag[CACHE_WAY * addr_set + 1] = addr_tag;
        end
    endtask

    always @(posedge CLK or posedge RESET) begin
        if (RESET) begin
            reset();
        end
        if (C1 != 0 && command_is_running == 0) begin
            cur_command = C1;
            command_is_running = 1;
            addr_tag = A1[CACHE_TAG_SIZE-1:0];
            addr_set = A1[ADDR1_BUS_SIZE-1:CACHE_TAG_SIZE];
            #2 addr_offset = A1[CACHE_OFFSET_SIZE-1:0];
            if (cur_command == C1_READ8) begin
                if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
                    || valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
                    || valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
                    || tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
                        read_data_from_mem();
                end
                else begin
                    #12 cache_hits_count++;
                end
                read_data_from_cache(8);
                #2 C1_write_enabled = 0;
                D1_write_enabled = 0;
            end 
            else if (cur_command == C1_READ16) begin
                if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
                    || valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
                    || valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
                    || tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
                        read_data_from_mem();
                end
                else begin
                    #12 cache_hits_count++;
                end
                read_data_from_cache(16);
                #2 C1_write_enabled = 0;
                D1_write_enabled = 0;
            end
            else if (cur_command == C1_READ32) begin
                if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
                    || valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
                    || valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
                    || tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
                        read_data_from_mem();
                end
                else begin
                    #12 cache_hits_count++;
                end
                read_data_from_cache(32);
                #2 C1_write_enabled = 0;
                D1_write_enabled = 0;
            end
            else if (cur_command == C1_INVALIDATE_LINE) begin
                if (tag[CACHE_WAY * addr_set] == addr_tag) begin
                    invaildate_line(addr_set * CACHE_WAY);
                end
                else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
                    invaildate_line(addr_set * CACHE_WAY + 1);
                end
                #2 C1_write_enabled = 0;
            end
            else if (cur_command == C1_WRITE8) begin
                write_data_in_cache(8);
            end
            else if (cur_command == C1_WRITE16) begin
                write_data_in_cache(16);
            end
            else begin
                write_data_in_cache(32);
            end
            command_is_running = 0;
        end
    end

   // always @(negedge CLK) begin // For reading data
    //end
endmodule : cache

