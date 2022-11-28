`include "cache.sv"

import utility::*;
module testbench();
    input CLK;
    input RESET;
    inout wire[ADDR1_BUS_SIZE] A1;
    inout wire[ADDR2_BUS_SIZE] A2;
    inout wire[DATA1_BUS_SIZE] D1;
    inout wire[DATA2_BUS_SIZE] D2;
    inout wire[CTR1_BUS_SIZE] C1;
    inout wire[CTR2_BUS_SIZE] C2;
    cache_cpu c(CLK, RESET, A1, A2, D1, D2, C1, C2);
    cache_address_t address;

    initial begin
        c.init();
        address.set = 0;
        address.tag = 0;
        address.offset = 0;
        c.execute_command_from_CPU(1, address);
        address.set = 0;
        address.tag = 1;
        address.offset = 0;
        c.execute_command_from_CPU(2, address);
        c.execute_command_from_CPU(4, address);
        c.execute_command_from_CPU(3, address);
    end
endmodule
