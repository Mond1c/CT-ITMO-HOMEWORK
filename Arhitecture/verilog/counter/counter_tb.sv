`include "counter.sv"

module counter_tb;
    reg clk, reset, enable;
    wire[3:0] out;

    counter c4(out, clk, reset, enable);

    initial begin
        $dumpfile("dump.vcd");
        $dumpvars(1, counter_tb);

      	clk = 0;
      	reset = 1;
        enable = 1;
        #1 reset = 0;
        #2 reset = 1;
        #2 reset = 0;
      	#20 $finish;

    end

    always #1 clk = ~clk;
  
    always @(posedge clk) $display("%0t\t%b %b %b %b == %d", $time, clk, reset, enable, out, out);
endmodule