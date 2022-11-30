`include "new_cache.sv"

module test2();
    input wire                        CLK;
    input wire                        RESET;
    input wire[13:0]    A1;
    input wire[13:0]    A2;
    inout wire[15:0]    D1;
    inout wire[15:0]    D2;
    inout wire[2:0]     C1;
    inout wire[1:0]     C2;

    new_cache ch(CLK, RESET, A1, A2, D1, D2, C1, C2);

    reg clk;
    reg[2:0] c1;
    reg[13:0] a1;
    assign CLK=clk;
    assign C1=c1;
    assign A1=a1;

    initial begin
        $monitor("time=%0d\t%0d\t%b %0d", $time(), C1, D1, $size(D1));
		clk = 1;    	
		c1 = 3;
		a1[8:0] = 1;
		a1[14:8] = 0;
		#1 a1[7:0] = 0;
        c1='bz;
    end
endmodule