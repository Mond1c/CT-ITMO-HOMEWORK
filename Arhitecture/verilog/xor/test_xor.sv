`include "xor.sv"

module xor2_test;
    reg in1, in2;
    wire out;

    xor2 xor2var(out, in1, in2);

    initial begin
        $monitor("%0t: in1: %b, in2: %b, out = %b", $time, in1, in2, out);
        in1 = 0; in2 = 0;
        #1 in1 = 0; in2 = 1;
        #1 in1 = 1; in2 = 0;
        #1 in1 = 1; in2 = 1;
    end
endmodule
