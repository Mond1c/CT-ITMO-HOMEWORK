module test #(parameter _SEED = 225526);
  integer SEED = _SEED;
  reg[7:0] a[0:99];
  integer i = 0;
  initial begin    
    for (i = 0; i < 100; i += 1) begin
      a[i] = $random(SEED)>>16;  
    end
   
    for (i = 0; i < 100; i += 1) begin
      $display("[%d] %d", i, a[i]);  
    end
   
    $finish;
  end 
endmodule
