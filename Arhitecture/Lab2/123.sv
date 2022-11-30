module and(input CLK, inout a, inout b, inout c);
    always @(posedge CLK) begin
        c = a & b;
    end
endmodule : and

module and_test();
    reg CLK;
    reg a;
    reg b;
    reg c;

    and(CLK, a, b, c);

    initial begin
        CLK = 1;
    end
endmodule : and_test