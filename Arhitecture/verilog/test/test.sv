`include "pack.sv"

import test::*;
module ok;
    CPU cpu;

    initial begin
        pair p;
        p.first = 1;
        p.second = 2;
        $display("%d %d", p.first, p.second);
        cpu = new(1);
        cpu.display();
    end
endmodule