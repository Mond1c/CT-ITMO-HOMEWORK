`include "constants.sv"

module CPU(
    output reg[CACHE_TAG_SIZE + CACHE_SET_SIZE - 1: 0] A1,
    inout wire[16-1:0] D1,
    inout wire[3-1:0] C1,
    input wire clk,

    output reg C_DUMP,
    output reg M_DUMP,
    output reg RESET
);

    // =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======
    reg[16-1:0]   D1_buffer; assign D1 = D1_buffer;
    reg[3 - 1:0]    C1_buffer; assign C1 = C1_buffer;
    // =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======
    // =======МЕТОДЫ ДЛЯ ОБЩЕНИЯ=========
    reg[8-1:0] buffer[CACHE_LINE_SIZE - 1:0];
    reg[CACHE_ADDR_SIZE - 1:0] address;

    task waitToWrite();
        if (clk == 1)   @(negedge clk);
    endtask

    task C1_READ8();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 1;
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  A1 = 'z; C1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        buffer[0] = D1 % (1 << 8);

        @(negedge clk)  C1_buffer = 0;
    endtask

    task C1_READ16();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 2;
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  A1 = 'z; C1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        buffer[0] = D1 >> 8; buffer[1] = D1 % (1 << 8);

        @(negedge clk)  C1_buffer = 0;
    endtask

    task C1_READ32();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 3;
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  A1 = 'z; C1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        buffer[0] = D1 >> 8; buffer[1] = D1 % (1 << 8);
        @(posedge clk)  buffer[2] = D1 >> 8; buffer[3] = D1 % (1 << 8);

        @(negedge clk)  C1_buffer = 0;
    endtask

    task C1_WRITE8();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 5;
        D1_buffer = buffer[0];
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  A1 = 'z; C1_buffer = 'z; D1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        @(negedge clk)  C1_buffer = 0;
    endtask

    task C1_WRITE16();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 6;
        D1_buffer = {buffer[0], buffer[1]};
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  A1 = 'z; C1_buffer = 'z; D1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        @(negedge clk)  C1_buffer = 0;
    endtask

    task C1_WRITE32();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 7;
        D1_buffer = {buffer[0], buffer[1]};
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        D1_buffer = {buffer[2], buffer[3]};
        @(negedge clk)  A1 = 'z; C1_buffer = 'z; D1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        @(negedge clk)  C1_buffer = 0;
    endtask

    task C1_INVALIDATE_LINE();
        waitToWrite();
        A1 = address >> CACHE_OFFSET_SIZE; C1_buffer = 1;
        @(negedge clk)  A1 = address % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  A1 = 'z; C1_buffer = 'z;

        do 
            @(posedge clk);
        while (C1 != 7);

        @(negedge clk)  C1_buffer = 0;
    endtask
    // =======МЕТОДЫ ДЛЯ ОБЩЕНИЯ=========
    // ======================
    localparam M = 64;
    localparam N = 60;
    localparam K = 32;

    integer a = 0; // указатели на начала массивов
    integer b = a + M * K;
    integer c = b + K * N * 2;

    integer pa; integer pa_k; 
    integer pb; integer pb_x; 
    integer pc; integer pc_x; 

    integer s;

    integer ticks;

    always @(negedge clk)   ticks += 1;

    initial begin
        C1_buffer = 0; D1_buffer = 'z; A1 = 'z; ticks = 0;
        // ============================MAIN=================================
        pa = a; @(negedge clk); 
        pc = c; @(negedge clk); 
        @(negedge clk);  // integer y = 0
        for (integer y = 0; y < M; y += 1) begin
            @(negedge clk); // y < M
            @(negedge clk);// integer x = 0
            for (integer x = 0; x < N; x += 1) begin
                @(negedge clk); // x < N
                pb = b; @(negedge clk);
                s = 0;  @(negedge clk); 
                @(negedge clk); // integer k = 0;
                for (integer k = 0; k < K; k += 1) begin
                    @(negedge clk); // k < K
                    address = pa + k; C1_READ8();
                    pa_k = buffer[0];
                    address = pb + x * 2; C1_READ16();
                    pb_x = {buffer[0], buffer[1]};
                    repeat (6) @(negedge clk);  s += pa_k * pb_x;
                    pb += N * 2; @(negedge clk);
                    @(negedge clk);  // k++
                end
                buffer[0] = (s >> 24); buffer[1] = (s >> 16) % (1 << 8); buffer[2] = (s >> 8) % (1 << 8); buffer[3] = s % (1 << 8);
                address = pc + (x * 4); C1_WRITE32();
                @(negedge clk);     // x++
            end
            @(negedge clk); pa += K;
            @(negedge clk); pc += N * 4;
            @(negedge clk);  // y++
        end

        @(negedge clk); // выход из функции

        @(posedge clk) $display("HITS: %d MISSES: %d TOTAL: %d TICKS: %d", cache.HITS, cache.MISSES, cache.HITS + cache.MISSES, ticks);

        $finish();
        // ============================MAIN=================================
    end

endmodule