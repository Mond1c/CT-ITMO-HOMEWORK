`include "and.sv"

module test_and;
    reg in1, in2;
    wire out;

    and2 and2var(out, in1, in2);

    initial begin
        $monitor("%0t: in1 = %b, in2 = %b, out = %b", $time, in1, in2, out);
        in1 = 0; in2 = 0;
        #1 in1 = 0; in2 = 1;
        #1 in1 = 1; in2 = 0;
        #1 in1 = 1; in2 = 1;
    end
endmodule
