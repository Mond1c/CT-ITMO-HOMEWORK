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
    input                           C_DUMP,
    input                           M_DUMP,
    input wire[ADDR1_BUS_SIZE-1:0]  A1,
    inout wire[DATA1_BUS_SIZE-1:0]  D1,
    inout wire[CTR1_BUS_SIZE-1:0]   C1
    );
    output wire[ADDR2_BUS_SIZE-1:0] A2;
    inout wire[DATA2_BUS_SIZE-1:0] D2;
    inout wire[CTR2_BUS_SIZE-1:0] C2;

    int cache_hits_count = 0;
    int cache_miss_count = 0;
    bit valid[CACHE_LINE_COUNT];
    bit dirty[CACHE_LINE_COUNT];
    reg[CACHE_LINE_SIZE-1:0] data[CACHE_LINE_COUNT];
    reg[CACHE_TAG_SIZE-1:0] tag[CACHE_LINE_COUNT];
    bit lru[CACHE_LINE_COUNT];

    mem_ctr mem(CLK, RESET, M_DUMP, A2, D2, C2);

    reg[ADDR1_BUS_SIZE-1:0] a1='bz;
    reg[DATA1_BUS_SIZE-1:0] d1='bz;
    reg[CTR1_BUS_SIZE-1:0] c1='bz;
    reg[ADDR2_BUS_SIZE-1:0] a2='bz;
    reg[DATA2_BUS_SIZE-1:0] d2='bz;
    reg[CTR2_BUS_SIZE-1:0] c2='bz;

    assign A1 = a1;
    assign D1 = d1;
    assign C1 = c1;
    assign A2 = a2;
    assign D2 = d2;
    assign C2 = c2;

    reg[CACHE_TAG_SIZE-1:0] addr_tag;
    reg[CACHE_SET_SIZE-1:0] addr_set;
    reg[CACHE_OFFSET_SIZE-1:0] addr_offset;

    int one_tick = 2;
    int index1 = 0;
    int index2 = 0;
    int cur_command = 0;
    int count = 0;

    initial begin
        //$monitor("%b", D1);
        $dumpfile("dump_cache.vcd");
        $dumpvars(0, cache);
        $dumpoff;
    end

    function int get_count();
        return count;
    endfunction

    function void reset();
        cache_hits_count = 0;
        cache_miss_count = 0;
        for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
            valid[i] = 0;
            dirty[i] = 0;
            data[i] = 0;
            tag[i] = 'bz;
            lru[i] = 0;
        end
    endfunction

    function void cache_info();
    $display(count);
        $display("Requests = %d", cache_miss_count + cache_hits_count);
        $display("Cache hits = %d", cache_hits_count);
        $display("Cache misses = %d", cache_miss_count);
        $display("Cache hit ratio = %f", (1.0 * cache_hits_count) / (1.0 * (cache_hits_count + cache_miss_count)));
    endfunction

    task read_from_cache(int index, int data_size);
    //$display(data_size);
        case(data_size)
            8: begin
                d1[7:0] = data[index][addr_offset +: 8];
                //$display(D1);
            end
            16: begin
                d1 = data[index][addr_offset +: 16];
            end
            32: begin
                d1 = data[index][addr_offset +: 16];
                #one_tick d1 = data[index][addr_offset+16+:16];
            end
        endcase
    endtask

    task write_in_cache(int index, int data_size);
        case(data_size)
            8: begin
                data[index][addr_offset +: 8] = D2[7:0];
                //$display(123);
            end
            16: begin
                data[index][addr_offset +: 16] = D2;
            end
            32: begin
                data[index][addr_offset +: 16] = D2;
                #one_tick data[index][addr_offset+16 +: 16] = D2;
            end
        endcase
        dirty[index] = 1;
    endtask

    task write_data_in_mem(int index);
        c2 = C2_WRITE_LINE;
        a2 = {addr_tag, addr_set};
        //wait (C2 == C2_RESPONSE);
        for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
            #(i > 0 ? one_tick : 0) d2 = data[index][i +: DATA2_BUS_SIZE];
        end
        #200 c2 = 'bz;
        dirty[index] = 0;
        d2 = 'bz;
        count += 100;
    endtask

    task read_data_from_mem(int index);
        if (valid[index] == 1 && dirty[index] == 1) begin
            write_data_in_mem(index);
        end
        c2 = C2_READ_LINE;
        a2 = {addr_tag, addr_set};
        #200 c2 = 'bz; 
      //  $display("ok");
        //wait (C2 == C2_RESPONSE);
      //  $display("ok");
        valid[index] = 1;
        tag[index] = addr_tag;
        lru[index] = 1;
        for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
           // $display("Take = %0b", D2);
            #(i > 0 ? one_tick : 0) data[index][i +: DATA2_BUS_SIZE] = D2;
        end
        count += 100;
        //$display("%b", data[index]);
    endtask



    task read(int data_size);
        index1 = addr_set * CACHE_WAY;
        index2 = addr_set * CACHE_WAY + 1;
        if (valid[index1] == 1 && tag[index1] == addr_tag) begin
            lru[index1] = 1;
            lru[index2] = 0;
            cache_hits_count++;
            c1 = C1_RESPONSE;
            count += 6;
            #12 read_from_cache(index1, data_size);
        end
        else if (valid[index2] == 1 && tag[index2] == addr_tag) begin
            lru[index2] = 1;
            lru[index1] = 0;
            cache_hits_count++;
            c1 = C1_RESPONSE;
            count += 6;
            #12 read_from_cache(index2, data_size);
        end
        else if (valid[index1] == 0) begin
            count += 4;
            cache_miss_count++;
            lru[index2] = 0;
            #8 read_data_from_mem(index1);
            //$display("1234");
            c1 = C1_RESPONSE;
            read_from_cache(index1, data_size);
        end
        else if (valid[index2] == 0) begin
            count += 4;
            cache_miss_count++;
            lru[index1] = 0;
            #8 read_data_from_mem(index2);
            c1 = C1_RESPONSE;
            read_from_cache(index2, data_size);
        end
        else if (lru[index1] < lru[index2]) begin
            count += 4;
            cache_miss_count++;
            #8 read_data_from_mem(index1);
            c1 = C1_RESPONSE;
            read_from_cache(index1, data_size);
        end
        else begin
            count += 4;
            cache_miss_count++;
            #8 read_data_from_mem(index2);
            c1 = C1_RESPONSE;
            read_from_cache(index2, data_size);
        end
        #2 c1 = 'bz;
    endtask

    task write(int data_size);
        index1 = addr_set * CACHE_WAY;
        index2 = addr_set * CACHE_WAY + 1;
        if (valid[index1] == 1 && tag[index1] == addr_tag) begin
            count += 6;
            #12 cache_hits_count++;
            lru[index1] = 1;
            lru[index2] = 0;
            c1 = C1_RESPONSE;
            #1 write_in_cache(index1, data_size);
        end
        else if (valid[index2] == 1 && tag[index2] == addr_tag) begin
            count += 6;
            #12 cache_hits_count++;
            lru[index2] = 1;
            lru[index1] = 0;
            c1 = C1_RESPONSE;
            #1 write_in_cache(index2, data_size);
        end
        else if (valid[index1] == 0) begin
            count += 4;
            #8 cache_miss_count++;
            lru[index2] = 0;
            read_data_from_mem(index1);
            c1 = C1_RESPONSE;
            #1 write_in_cache(index1, data_size);
        end
        else if (valid[index2] == 0) begin
            count += 4;
            #8 cache_miss_count++;
            lru[index1] = 0;
            read_data_from_mem(index2);
            c1 = C1_RESPONSE;
            #1 write_in_cache(index2, data_size);
        end
        else if (lru[index1] < lru[index2]) begin
            count += 4;
            #8 cache_miss_count++;
            read_data_from_mem(index1);
            c1 = C1_RESPONSE;
            #1 write_in_cache(index1, data_size);
        end
        else begin
            count += 4;
            #8 cache_miss_count++;
            read_data_from_mem(index2);
            c1 = C1_RESPONSE;
            #1 write_in_cache(index2, data_size);
        end
        #2 c1 = 'bz;
    endtask

    task invalidate_line;
        static int index1 = addr_set * CACHE_WAY;
        static int index2 = addr_set * CACHE_WAY + 1;
        if (tag[index1] == addr_tag) begin
            if (dirty[index1] == 1) begin
                write_data_in_mem(index1);
            end
            c1 = C1_RESPONSE;
            valid[index1] = 0;
            tag[index1] = 0;
            data[index1] = 0;
        end
        else if (tag[index2] == addr_tag) begin
            if (dirty[index2] == 1) begin
                write_data_in_mem(index2);
            end
            c1 = C1_RESPONSE;
            valid[index2] = 0;
            tag[index2] = 0;
            data[index2] = 0;
        end
        #2 c1 = 'bz;
    endtask

    always @(posedge CLK or posedge RESET or posedge C_DUMP) begin
     //y(123);
     //$display("%d", C1);
     //$display(C1);
        if (RESET) begin
            reset();
        end
        if (C_DUMP) begin
            $dumpon;
        end
        else begin
            $dumpoff;
        end 
        if (C1 != 0) begin
            cur_command = C1;
         //   $display("time = %d C1 = %d", $time(), C1);
            addr_tag = A1[7:0];
            addr_set = A1[13:8];
            #one_tick addr_offset = A1[6:0];
            case (cur_command)
                C1_READ8: read(8);
                C1_READ16: read(16);
                C1_READ32: read(32);
                C1_INVALIDATE_LINE: invalidate_line();
                C1_WRITE8: write(8);
                C1_WRITE16: write(16);
                C1_WRITE32: write(32);
            endcase
          //  $display("%0d %0d", $time(), C1);
            //$display($time());
        end
    end
endmodule : cache