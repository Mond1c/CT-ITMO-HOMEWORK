module counter(
    output reg[3:0] out,
    input clk,
    input reset,
    input enable
   	);

  always @ (posedge clk) begin
    if (enable) begin
      if (reset) 
        out <= 0;       
      else 
      	out <= out + 1; 
    end else begin
      out <= out;
    end
  end
endmodule