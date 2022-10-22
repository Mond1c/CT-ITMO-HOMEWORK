module not_switch_test;
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

module nand2_test;
    reg in1, in2;
    wire out;

    nand2 nand2var(out, in1, in2);

    inital begin
        $monitor("%0t: in1: %b, in2: %b, out = %b", $time, in1, in2. out);
        in1 = 0; in2 = 0;
        #1 in1 = 0; in2 = 1;
        #1 in1 = 1; in2 = 0;
        #1 in1 = 1; in2 = 1;
    end

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