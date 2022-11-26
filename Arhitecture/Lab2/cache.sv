`include "utility.sv"

import utility::*;
module cache_cpu(        
    input CLK,
    input RESET,
    inout wire[ADDR1_BUS_SIZE] A1,
    inout wire[ADDR2_BUS_SIZE] A2,
    inout wire[DATA1_BUS_SIZE] D1,
    inout wire[DATA2_BUS_SIZE] D2,
    inout wire[CTR1_BUS_SIZE] C1,
    inout wire[CTR2_BUS_SIZE] C2);

    // Cache
    cache_line[CACHE_LINE_COUNT] lines;
    cache_line line;
    bit data[];
    integer SEED = 10000;

    function void init();
        integer set_number = 0;
        for (integer i = 0; i < CACHE_LINE_COUNT; i++) begin
            if (set_number % 2 == 0 && set_number != 0) begin
                set_number += 1;
            end
            line.valid = 1;
            line.dirty = 0;
            line.tag = i + set_number;
            line.data = $random(SEED);
            lines[i] = line;
        end
    endfunction

    function cache_line get_cache_line(cache_address address);
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

    function void write_into_cache_line(cache_address address);
        int i = address.set * CACHE_WAY;
        if (lines[i].tag == address.tag) begin
            line.valid = lines[i].valid;
            line.dirty = lines[i].dirty;
            line.tag = lines[i].tag;
            for (int j = 0; j < CACHE_LINE_SIZE; j++) begin
                line.data[j] = data[j];
            end
            lines[i] = line;
        end
        else if (lines[i + 1].tag == address.tag) begin
            line.valid = lines[i + 1].valid;
            line.dirty = lines[i + 1].dirty;
            line.tag = lines[i + 1].tag;
            for (int j = 0; j < CACHE_LINE_SIZE; j++) begin
                line.data[j] = data[j];
            end
            lines[i + 1] = line;
        end
        else begin
            $error("It's illigal address");
        end
    endfunction

    function void execute_command_from_CPU(byte command, cache_address address);
        case(command)
            0: begin
                $display("C1_NOP");
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
                $display("C2_READ32");
            end
            4: begin
                write_into_cache_line(address);
                $display("line.data = %b", line.data); 
                $display("C1_INVALIDATE_LINE");
            end
            5: begin
                $display("C1_WRITE8");
            end
            6: begin
                $display("C1_WRITE16");
            end
            7: begin
                $display("C1_WRITE32");
            end
        endcase
    endfunction
endmodule
