`include "cache.sv"

module cache_tb #(parameter _SEED = 225526) ();
    input wire                        CLK;
    input wire                        RESET;
    input wire[13:0]    A1;
    inout wire[15:0]    D1;
    inout wire[2:0]     C1;

    cache ch(CLK, RESET, A1, D1, C1);

    reg clk=0;
    reg[2:0] c1;
    reg[13:0] a1;
    reg[15:0] d1;
    assign CLK=clk;
    assign C1=c1;
    assign A1=a1;
    assign D1=d1;

    int M = 64;
    int N = 60;
    int K = 32;

    byte a[M][K];
    shortint b[K][N];
    int c[M][N];

    byte pa = 0;
    shortint pb = 0;
    int pc = 0;


    int SEED = _SEED;

    reg a = 1;

    task program;
        // generate a random data set
        for (int i = 0; i < M; i++) begin
            for (int j = 0; j < K; j++) begin
               a[i][j] = $random(SEED); 
           end
        end
        for (int i = 0; i < K; i++) begin
            for (int j = 0; j < N; j++) begin
                b[i][j] = $random(SEED);
            end
        end
        for (int i = 0; i < M; i++) begin
            for (int j = 0; j < N; j++) begin
                c[i][j] = $random(SEED);
            end
        end
        // program:
        
    endtask

    initial begin // TODO: Так как clk - это синхрониззация то вся задержка умножается на 2
        $monitor("clk=%d, time=%0d\tC1=%d\tD1=%b", clk, $time(), C1, D1);
        #1 d1 = 'bz;
        c1 = 1;
        a1[7:0] = 1;
        a1[13:8] = 0;
        #2 a1[6:0] = 0;
        c1 = 'bz;
        #226 c1 = 4;
        a1[7:0] = 1;
        a1[13:8] = 0;
        #2 a1[6:0] = 0;
        c1 = 'bz;
        #18 c1 = 1;
        d1 = 'bz;
        a1[7:0] = 1;
        a1[13:8] = 0;
        #2 a1[6:0] = 0;
        c1 = 'bz;
        /*
        clk = 1;
        d1 = 'bz;
        c1 = 1;
        a1[7:0] = 1;
        a1[13:8] = 0;
        #1 a1[6:0] = 0;
        c1 = 'bz;
        clk=0;
        #200    
        clk=0; 
        #1 clk=1;
        c1 = 4;
        a1[7:0] = 1;
        a1[13:8] = 0;
        //d1= 8'b10101010;
        #1 a1[6:0] = 0;
        c1 = 'bz;
        clk = 0;
        #8
        clk=1;
        d1='bz;
        c1=1;
        a1[7:0] = 1;
        a1[13:8] = 0;
        #1 a1[6:0] = 0;
        c1 = 'bz;
        */
    end

    always #1 if ($time() < 600) begin
        clk = ~clk;
    end
endmodule
