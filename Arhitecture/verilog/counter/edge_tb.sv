module edge_tb;
  reg  a, b, c, q;

  initial begin
    $monitor("[%0t] a=%0b b=%0b c=%0b q=%0b", $time, a, b, c, q);
    a <= 0;
    b <= 0;
    c <= 0;
    q <= 0;
    #5 a = 1; c = 1;
    q = #5 a & b | c;
    #1 q = 0;
    #10;
  end

  always @(posedge a) $display("[%0t] a=%0b [posedge]", $time, a);
  always @(negedge b) $display("[%0t] b=%0b [negedge]", $time, b);
  always #2 begin
    c = @(posedge q) (a & b);
    $display("[%0t] c=%0b [posedge =]", $time, c);
  end
  always #1 begin
    c = @(negedge q) (a | 1);
    $display("[%0t] c=%0b [negedge =]", $time, c);
  end
  
endmodule