`include "constants.sv"
`include "memory.sv"
`include "cache.sv"
`include "CPU.sv"

module testbench();
    wire [16-1:0] D2;
    wire [2-1:0] C2;
    wire [CACHE_TAG_SIZE + CACHE_SET_SIZE - 1 : 0]  A2;

    wire [16-1:0] D1;
    wire [3-1:0] C1;
    reg [CACHE_TAG_SIZE + CACHE_SET_SIZE - 1 : 0]  A1;

    reg clk; wire RESET; wire M_DUMP; wire C_DUMP;

    Memory mem(D2, C2, A2, clk, RESET, M_DUMP);
    Cache cache(A1, D1, C1, A2, D2, C2, clk, RESET, C_DUMP);
    CPU cpu(A1, D1, C1, clk, C_DUMP, M_DUMP, RESET);

    always #1 clk = ~clk;

    initial begin
        clk = 0;
    end
endmodule