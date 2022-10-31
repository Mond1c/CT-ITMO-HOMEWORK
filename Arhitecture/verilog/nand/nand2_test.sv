`include "nand2.sv"

module nand2_test;
    reg in[1:0];
    wire out;

    nand2 nand2var(out, in[1], in[0]);

    initial begin
        $dumpfile("dump.vcd");
        $dumpvars(1);
        $monitor("%0t: in1: %b, in2: %b, out = %b", $time, in[1], in[0], out);
        // in[1] = 0; in[0] = 0;
        // #1 in[1] = 0; in[0] = 1;
        // #1 in[1] = 1; in[0] = 0;
        // #1 in[1] = 1; in[0] = 1;
        for (in = 0; in < 3; in = in + 1)
            #(in);
    end
endmodule