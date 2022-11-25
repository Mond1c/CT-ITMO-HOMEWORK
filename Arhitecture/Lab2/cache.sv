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

    function cache_line get_cache_line(cache_address address);
        integer set_number = address.set;
        if (lines[set_number * CACHE_WAY].tag == address.tag) begin
            return lines[set_number * CACHE_WAY];
        end
        if (lines[set_number * CACHE_WAY + 1].tag == address.tag) begin
            return lines[set_number * CACHE_WAY + 1];
        end
        $error("It's illigal address");
    endfunction

    function void execute_command_from_CPU(byte command, cache_address address);
        case(command)
            0: begin
                $display("C1_NOP");
            end
            1: begin
                cache_line line = get_cache_line(address);
                
                $display("C1_READ8");
            end
            2: begin
                $display("C1_READ16");
            end
            3: begin
                $display("C2_READ32");
            end
            4: begin
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