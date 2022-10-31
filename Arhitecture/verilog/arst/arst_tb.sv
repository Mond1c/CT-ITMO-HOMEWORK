`include "arst.sv"

module arst_tb;
    reg r, s;
    wire q, nq;

    async_rs_trigger rst(q, nq, r, s);

    initial begin
        $dumpfile("dump.vcd");
        $dumpvars(1, arst_tb);
        $monitor("%0t: r=%b s=%b q=%b", $time, r, s, q);

            r = 0; s = 0;
        #1; r = 0; s = 1;
        #1; r = 1; s = 1;
        #1; r = 1; s = 0;
        #1; r = 0; s = 1;
        #1; r = 1; s = 1;
        #1; r = 0; s = 0;
        #1; r = 1; s = 0;
        #1; r = 0; s = 0;
        $finish;
    end  
endmodule