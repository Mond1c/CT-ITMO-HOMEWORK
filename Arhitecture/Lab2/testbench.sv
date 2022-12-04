`include "another_cache.sv"

module cache_tb #(parameter _SEED = 225526) ();
    input wire                        CLK;
    input wire                        RESET;
    input wire[13:0]    A1;
    inout wire[15:0]    D1;
    inout wire[2:0]     C1;

    cache ch(CLK, RESET, A1, D1, C1);
    int SEED = _SEED;

    reg clk=0;
    reg[2:0] c1='bz;
    reg[13:0] a1='bz;
    reg[15:0] d1='bz;
    assign CLK=clk;
    assign C1=c1;
    assign A1=a1;
    assign D1=d1;
    localparam M = 64;
    localparam N = 60;
    localparam K = 32;

    int pa = 0;
    int pb = 0;
    int pc = 0;
    int s = 0;
    reg[7:0] a;
    reg[15:0] b;
    reg[31:0] c;
    int i = 0;
    int a_i = 0;
    int b_i = 0;
    int c_i = 0;
    int count = 0;

    initial begin 
        $display("Start");
        for (int y = 0; y < M; y++) begin
            for (int x = 0; x < N; x++) begin
                pb = 0;
                s = 0;
                for (int k = 0; k < K; k++) begin
                    if (a_i - pa * 128 >= 0) begin
                        pa++;
                    end
                    if (b_i - pb * 128 >= 0) begin
                        pb++;
                    end
                    d1 = 'bz;
                    c1 = 1;
                    a1[7:0] = pa;
                    a1[13:8] = pa % 64;
                    #2 a1[6:0] = a_i % 128;
                    c1 = 'bz;
                    count += 1;
                    #300
                    a = D1[7:0];
                    #2 d1 = 'bz;
                    c1 = 2;
                    a1[7:0] = pb + 129;
                    a1[13:8] = pb % 64;
                    #2 a1[6:0] = b_i % 128;
                    c1 = 'bz;
                   // count += 1;
                    #300
                    b = D1[15:0];
                    #2 d1 = 'bz;
                    s += a + b;
                    a_i += 8;
                    b_i += 16;
                end
                c = s;
                if (c_i - pc * 128 >= 0) begin
                    pc++;
                end
                #300 c1 = 7;
                 a1[7:0] = pc + 370;
                 a1[13:8] = pc % 64;
                 #2 a1[6:0] = c_i % 128;
                 c1 = 'bz;
                 #300
                 c_i += 32;
            end
            $display("$d", y);
        end
        $display(count);
        ch.cache_info();
        $finish;
    end

    always #1 if ($time() < 100000000) begin
        clk = ~clk;
    end
endmodule
