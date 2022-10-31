/*module not_switch_test;
    reg a;
    wire b;
    not_switch not1(b, a);
    // not_switch not1(.out(b), .in(a))

    initial begin
        $monitor("%0t %b", $time, b);
        a = 0;
        #1 a = 1;
        #1 a = 0;
    end
endmodule
*/
module nand2_test;
    reg[1:0] in;
    wire out;

    nand2 nand2var(out, in[1], in[0]);

    initial begin
        $monitor("%0t: in1: %b, in2: %b, out = %b", $time, in[1], in[0], out);
        in1 = 0; in2 = 0;
        #1 in1 = 0; in2 = 1;
        #1 in1 = 1; in2 = 0;
        #1 in1 = 1; in2 = 1;
    end
endmodule
/*
module not_switch_test;
    wire a, b;

    not_switch not1(b, a);

    initial begin
        $monitor("%b", b);
        assign a = 0;
        #1 assign a = 1;
        #1 assign a = 0;
    end
endmodule
*/
