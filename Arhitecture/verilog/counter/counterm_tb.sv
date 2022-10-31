`include "counterm.sv"

module counterm_tb #(parameter _N = 5);
    reg clk, reset, enable;
    wire[_N-1:0] out;
    
    counterm #(_N) _counterm(out, clk, reset, enable);   // mod 2**5 
    //wire[4-1:0] out4;
    //counterm _counterm(out4, clk, reset, enable); // mod 2**4 (default) 

    initial begin
        $dumpfile("dump.vcd");
        $dumpvars(1, counterm_tb);

      	clk = 0;
      	reset = 1;
        enable = 1;
        #1 reset = 0;
        #2 reset = 1;
        #2 reset = 0;      
      	#20 $finish;

    end

    always #1 clk = ~clk;
  
    always @(posedge clk) 
        $display("%0t\t%b %b %b %b == %d", $time, clk, reset, enable, out, out);
endmodule