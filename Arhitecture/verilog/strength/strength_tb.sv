`include "strength.sv"

module tb;
  reg i1, i2;
  wire out;
  
  strength st(out, i1, i2);
  
  initial begin
    $monitor("%0t: i1 = %b, i2 = %b -> out = %b", $time, i1, i2, out);
       i1 = 0; i2 = 0;
    #1 i1 = 0; i2 = 1;
    #1 i1 = 1; i2 = 0;
    #1 i1 = 1; i2 = 1;
  end
endmodule