`include "utility.sv"

import utility::*;
module cache_cpu #(   
    parameter MEM_SIZE          = 2097152,
    parameter CACHE_SIZE        = 16384,
    parameter CACHE_LINE_SIZE   = 128,
    parameter CACHE_LINE_COUNT  = 128,
    parameter CACHE_WAY         = 2,
    parameter CACHE_SETS_COUNT  = 64,
    parameter CACHE_TAG_SIZE    = 8,
    parameter CACHE_SET_SIZE    = 6,
    parameter CACHE_OFFSET_SIZE = 7,
    parameter CACHE_ADDR_SIZE   = 21,

    // Start parameters (bits) (bus)
    parameter DATA1_BUS_SIZE    = 16,
    parameter DATA2_BUS_SIZE    = 16,
    parameter ADDR1_BUS_SIZE    = 14,
    parameter ADDR2_BUS_SIZE    = 14,
    parameter CTR1_BUS_SIZE     = 3, // commands from 0 to 7 2^3 = 8
    parameter CTR2_BUS_SIZE     = 2, // commands from 0 to 3 2^2 = 4

    // Commands
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
    parameter C2_RESPONSE           = 1)
    (        
    input                           CLK,
    input                           RESET,
    input   wire[ADDR1_BUS_SIZE]    A1,
    output  wire[ADDR2_BUS_SIZE]    A2,
    inout   wire[DATA1_BUS_SIZE]    D1,
    inout   wire[DATA2_BUS_SIZE]    D2,
    inout   wire[CTR1_BUS_SIZE]     C1,
    inout   wire[CTR2_BUS_SIZE]     C2);
    
    // Variables
    cache_line_t[CACHE_LINE_COUNT]  lines;
    cache_line_t                    line;
    bit                             data[];
    bit                             data1[];
    bit                             data2[];
    int                             data1_size;
    int                             data2_size;
    int                             SEED = 10000;
    byte                            command_C1;
    byte                            command_C2;
    cache_address_t                 addr1;
    cache_address_t                 addr2;
    bit                             addr1_status=0;
    bit                             addr2_status=0;
    bit                             addr1_is_ready=0;
    bit                             addr2_is_ready=0;

    // read from buses
    
    always @(posedge CLK or posedge RESET) begin
        if (A1) begin
            if (addr1_status == 0) begin
                addr1.tag = A1[CACHE_TAG_SIZE:0];
                addr1.set = A1[CACHE_ADDR1_SIZE:CACHE_TAG_SIZE];
                addr1_status = 1;
            end
            else begin
                addr1.offset = A1[CACHE_OFFSET_SIZE:0];
                addr1_status = 0;
                addr1_is_ready = 1;
            end
        end
        if (A2) begin
            if (addr2_status == 0) begin
                addr2.tag = A2[CACHE_TAG_SIZE:0];
                addr2.set = A2[CACHE_ADDR2_SIZE:CACHE_TAG_SIZE];
                addr2_status = 1;
            end
            else begin
                addr2.offset = A2[CACHE_OFFSET_SIZE:0];
                addr2_status = 0;
                addr2_is_ready = 1;
            end
        end
        if (D1) begin
            static bit tmp[] = data1;
            data1 = new[data1_size + DATA1_BUS_SIZE];
            for (int i = 0; i < data1_size; i++) begin
                data1[i] = tmp[i];
            end
            for (int i = data1_size; i < data1_size + DATA1_BUS_SIZE; i++) begin
                data1[i] = D1[i - data1_size];
            end
            data1_size += DATA1_BUS_SIZE;
        end
        if (D2) begin
            static bit tmp[] = data2;
            data2 = new [data2_size + DATA2_BUS_SIZE];
            for (int i = 0; i < data2_size; i++) begin
                data2[i] = tmp[i];
            end
            for (int i = data2_size; i < data2_size + DATA2_BUS_SIZE; i++) begin
                data[i] = D2[i - data2_size];
            end
            data2_size += DATA2_BUS_SIZE;
        end
        if (C1) begin
            command_C1 = C1;
        end
        if (C2) begin
            command_C2 = C2;
        end
    end

    initial begin
        forever begin
            if (check_if_command_from_CPU_is_ready(command_C1)) begin
                execute_command_from_CPU(command_C1, addr1);
            end
            if (check_if_command_from_MEM_CTR_is_ready(command_C2)) begin
                execute_command_from_MEM_CTR(command_C2, addr2);
            end 
        end
    end

    function bit check_if_command_from_CPU_is_ready(byte command);
        if (command == C1_NOP) begin
            return 1;
        end
        if (command == C1_READ8 && addr1_is_ready == 1) begin
            addr1_is_ready = 0;
            return 1;
        end
        if (command == C1_READ16 && addr1_is_ready == 1) begin
            addr1_is_ready = 0;
            return 1;
        end
        if (command == C1_READ32 && addr1_is_ready == 1) begin
            addr1_is_ready = 0;
            return 1;
        end
        if (command == C1_INVALIDATE_LINE && addr1_is_ready == 1) begin
            return 1;
        end
        if (command == C1_WRITE8 && addr1_is_ready == 1 && data1_size == 8) begin
            return 1;
        end
        if (command == C1_WRITE16 && addr1_is_ready == 1 && data1_size == 16) begin
        end
        if (command == C1_WRITE32 && addr1_is_ready == 1 && data1_size == 32) begin
            return 1;
        end
        return 0;        
    endfunction

    function bit check_if_command_from_MEM_CTR_is_ready(byte command);
        if (command == C2_NOP) begin
            return 1;
        end
        if (command == C2_READ_LINE && addr2_is_ready == 1) begin
            return 1;
        end
        if (command == C2_WRITE_LINE && addr2_is_ready == 1 && data2_size == CACHE_LINE_SIZE) begin
            return 1;
        end
        return 0;
    endfunction

    function void init(); // for test
        static int set_number = 0;
        for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
            if (set_number % 2 == 0 && set_number != 0) begin
                set_number += 1;
            end
            line.valid  = 1;
            line.dirty  = 0;
            line.tag    = i + set_number;
            line.data   = $random(SEED);
            lines[i]    = line;
        end
    endfunction

    function void read_data_from_bus(bit number);
        if (number == 0) begin      
        end
        else begin
        end
        $error("Something went wrong!");
    endfunction

    function cache_line_t get_cache_line(cache_address_t address);
        if (lines[address.set * CACHE_WAY].tag == address.tag) begin
            return lines[address.set * CACHE_WAY];
        end
        if (lines[address.set * CACHE_WAY + 1].tag == address.tag) begin
            return lines[address.set * CACHE_WAY + 1];
        end
        $error("It's illigal address");
    endfunction

    function void get_data(reg[CACHE_LINE_SIZE] source, int offset, int size);
        data = new[size];
        for (int i = offset; i < offset + size; i++) begin
            data[i - offset] = source[i];
        end
    endfunction

    function void write_into_cache_line(cache_address_t address);
        static int i = address.set * CACHE_WAY;
        if (lines[i].tag == address.tag) begin
            line.valid  = lines[i].valid;
            line.dirty  = lines[i].dirty;
            line.tag    = lines[i].tag;
            for (int j = 0; j < CACHE_LINE_SIZE; j++) begin
                //line.data[j] = data[j];
            end
            lines[i] = line;
        end
        else if (lines[i + 1].tag == address.tag) begin
            line.valid  = lines[i + 1].valid;
            line.dirty  = lines[i + 1].dirty;
            line.tag    = lines[i + 1].tag;
            for (int j = 0; j < CACHE_LINE_SIZE; j++) begin
                //line.data[j] = data[j];
            end
            lines[i + 1] = line;
        end
        else begin
            $error("It's illigal address");
        end
    endfunction

    function int execute_command_from_CPU(byte command, cache_address_t address);
        case(command)
            0: begin
                $display("C1_NOP");
                return C1_NOP;
            end
            1: begin
                line = get_cache_line(address);
                if (line.valid == 0) begin
                    $error("This cache line is not valid");
                end
                get_data(line.data, address.offset, 8);
                $display("%b, %b, %b, ", line.valid, line.dirty, line.tag);
                foreach (data[i]) begin
                    $display("data[%0d] = %0b", i, data[i]);
                end
                $display("C1_READ8");
                return C1_RESPONSE;
            end
            2: begin
                line = get_cache_line(address);
                if (line.valid == 0) begin
                    $error("This cache line is not valid");
                end
                get_data(line.data, address.offset, 16);
                $display("%b, %b, %b, ", line.valid, line.dirty, line.tag);
                foreach (data[i]) begin
                    $display("data[%0d] = %0b", i, data[i]);
                end
                $display("C1_READ16");
                return C1_RESPONSE;
            end
            3: begin
                line = get_cache_line(address);
                if (line.valid == 0) begin
                    $error("This cache line is not valid");
                end
                get_data(line.data, address.offset, 32);
                $display("%b, %b, %b, ", line.valid, line.dirty, line.tag);
                foreach (data[i]) begin
                    $display("data[%0d] = %0b", i, data[i]);
                end
                $display("C1_READ32");
                return C1_RESPONSE;
            end
            4: begin
               // write_into_cache_line(address);
                $display("line.data = %b", line.data); 
                $display("C1_INVALIDATE_LINE");
                return C1_RESPONSE;
            end
            5: begin
                $display("C1_WRITE8");
                return C1_RESPONSE;
            end
            6: begin
                $display("C1_WRITE16");
                return C1_RESPONSE;
            end
            7: begin
                $display("C1_WRITE32");
                return C1_RESPONSE;
            end
        endcase
    endfunction

    function void execute_command_from_MEM_CTR(byte command, cache_address_t address);
    endfunction
endmodule
