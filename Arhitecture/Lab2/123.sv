module a;
    bit arr[100];
    integer SEED = 10000;
    initial begin
        for (int i = 0; i < 100; i++)
            arr[i] = $random(SEED);
        $display("%b", arr);
    end
 endmodule
