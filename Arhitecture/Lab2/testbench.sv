`include "cache.sv"

module cache_tb();
    input wire                        CLK;
    input wire                        RESET;
    input wire[13:0]    A1;
    inout wire[15:0]    D1;
    inout wire[2:0]     C1;

    cache ch(CLK, RESET, A1, D1, C1);

    reg clk;
    reg[2:0] c1;
    reg[13:0] a1;
    reg[15:0] d1;
    assign CLK=clk;
    assign C1=c1;
    assign A1=a1;
    assign D1=d1;

    reg a = 1;

    initial begin
        $monitor("clk=%0d time=%0d\t%0d\t%b %0d", clk, $time(), C1, D1, $size(D1));
        clk = 1;        
        c1 = 5;
        a1[7:0] = 1;
        a1[13:8] = 0;
        d1= 8'b10101010;
        #1 a1[6:0] = 0;
        c1 = 'bz;
        clk=0;
        #8
        clk=1;
        d1='bz;
        c1=1;
        a1[7:0] = 1;
        a1[13:8] = 0;
        #1 a1[6:0] = 0;
        c1 = 'bz;
    end
endmodule