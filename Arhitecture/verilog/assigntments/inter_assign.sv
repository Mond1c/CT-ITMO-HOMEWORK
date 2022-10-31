module inter_assignment_delays_tb;
  reg  a, b, c, q;

  initial begin
    $monitor("INTER\t[%0t] a=%0b b=%0b c=%0b q=%0b", $time, a, b, c, q);
    a <= 0;
    b <= 0;
    c <= 0;
    q <= 0;
    #5 a <= 1; c <= 1;
    #5 q <= a & b | c;
    #20;
  end

endmodule

