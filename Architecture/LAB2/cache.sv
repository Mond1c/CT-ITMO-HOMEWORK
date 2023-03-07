`include "constants.sv"
`include "memory.sv"

module Cache(
    input wire[CACHE_TAG_SIZE + CACHE_SET_SIZE - 1 : 0] A1,
    inout wire[16 - 1 : 0] D1,
    inout wire[3 - 1 : 0] C1,
    output reg[CACHE_TAG_SIZE + CACHE_SET_SIZE - 1 : 0] A2,
    inout wire[16 - 1 : 0] D2,
    inout wire[2 - 1 : 0] C2,

    input wire clk,
    input wire RESET,
    input wire C_DUMP
);

    // =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======
    reg[16 - 1:0] D1_buffer; assign D1 = D1_buffer; 
    reg[3 - 1:0]  C1_buffer; assign C1 = C1_buffer;
    reg[16 - 1:0] D2_buffer; assign D2 = D2_buffer;
    reg[2 - 1:0]  C2_buffer; assign C2 = C2_buffer;
    // =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======
    // =======МЕТОДЫ ДЛЯ ОБЩЕНИЯ С ПАМЯТЬЮ=======
    task C2_READLINE();
        if (clk == 1)       @(negedge clk); 
        C2_buffer = 2;
        A2 = {tag, set}; 
        D2_buffer = 'z;

        @(negedge clk) C2_buffer = 'z; A2 = 'z;
        do
            @(posedge clk);
        while (C2 != 1);
        // дождались ответа


    for (integer j = 0; j < CACHE_LINE_SIZE; j += 2) begin
        Data[set][way][j] = D2 >> 8; Data[set][way][j + 1] = D2 % (1 << 8);
        @(negedge clk);
        if (j == CACHE_LINE_SIZE - 2) begin
            C2_buffer = 0; 
        end else @(posedge clk);
    end
    endtask

    task C2_WRITELINE();
        if (clk == 1) @(negedge clk);
        C2_buffer = 3; 
        A2 = {Tag[set][way], set};
        for (integer j = 0; j < CACHE_LINE_SIZE; j += 2) begin
            D2_buffer = {Data[set][way][j], Data[set][way][j + 1]}; 
            @(negedge clk);
            A2 = 'z;
        end
        D2_buffer = 'z; C2_buffer = 'z;
        do 
            @(posedge clk);
        while (C2 != 1);
        // мы дождались ответа
       @(negedge clk)   C2_buffer = 0; // забираем управление
    endtask
    // =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======
    // =======РЕГИСТРЫ, НУЖНЫЕ ДЛЯ ПОИСКА ЛИНИИ В КЭШЕ=========
    reg[CACHE_TAG_SIZE - 1 : 0] tag; reg[CACHE_SET_SIZE - 1 : 0] set; reg[CACHE_OFFSET_SIZE - 1 : 0] offset; reg[2 - 1 : 0] way;
    reg[8 - 1: 0]   buffer[4 - 1:0];

    reg cache_hit, found_empty; 
    // =======РЕГИСТРЫ, НУЖНЫЕ ДЛЯ ПОИСКА ЛИНИИ В КЭШЕ=========
    // ===========МЕТОДЫ ДЛЯ ОБЩЕНИЯ С ПРОЦЕССОРОМ=============

    // ищет линию с поданными tag и set
    // если найти линию не сможет, то будет считывать из памяти (вытесняя при необходимости)
    // по итогу присваивает в регистр way место в блоке, где лежит запрашиваемая линия
    task find_cache_line(); 
        cache_hit = 0;

        for (integer __way = 0; __way < CACHE_WAY; ++__way) begin
            if (Valid[set][__way] == 1 && Tag[set][__way] == tag && cache_hit == 0) begin
                cache_hit = 1;
                HITS += 1;

                way = __way; 
                Displacing[set] = 1 - way;
                repeat (CACHE_HIT_TIME - 2) @(negedge clk); // было исходно потрачено 2 такта на
                // передачу адресов\данных\команды => здесь их вычитаем
            end
        end

        if (cache_hit == 0) begin
            found_empty = 0;
            MISSES += 1;
            repeat (CACHE_MISS_TIME - 2) @(negedge clk);

            for (integer __way = 0; __way < CACHE_WAY; __way += 1) begin
                if (Valid[set][__way] == 0 && found_empty == 0) begin
                    found_empty = 1;

                    way = __way; 
                    Valid[set][way] = 1; 
                    Tag[set][way] = tag;
                    Dirty[set][way] = 0;
                    Displacing[set] = 1 - way;
                    C2_READLINE();
                end
            end

            if (found_empty == 0) begin
                way = Displacing[set];
                if (Dirty[set][way] == 1) begin
                    C2_WRITELINE();
                end
                C2_READLINE();
                Displacing[set] = 1 - way; 
                Valid[set][way] = 1;
                Tag[set][way] = tag;
                Dirty[set][way] = 0; 
            end
        end
    endtask

    task C1_READ8(); 
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  C1_buffer = 0;

        find_cache_line();
        C1_buffer = 7;  
        D1_buffer = Data[set][way][offset];
        @(negedge clk)  C1_buffer = 'z; D1_buffer = 'z;
    endtask

    task C1_READ16();
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  C1_buffer = 0;

        find_cache_line();
        C1_buffer = 7;  
        D1_buffer = {Data[set][way][offset], Data[set][way][offset + 1]};
        @(negedge clk)  C1_buffer = 'z; D1_buffer = 'z;
    endtask 

    task C1_READ32();
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  C1_buffer = 0;

        find_cache_line();
        C1_buffer = 7;  
        D1_buffer = {Data[set][way][offset], Data[set][way][offset + 1]};
        @(negedge clk)  D1_buffer = {Data[set][way][offset + 2], Data[set][way][offset + 3]};
        @(negedge clk)  C1_buffer = 'z; D1_buffer = 'z;
    endtask 

    task C1_WRITE8();
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        buffer[0] = D1 % (1 << 8);
        
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  C1_buffer = 0;

        find_cache_line();
        Dirty[set][way] = 1;

        Data[set][way][offset] = buffer[0];

        C1_buffer = 7;
        @(negedge clk) C1_buffer = 'z; D1_buffer = 'z;
    endtask

    task C1_WRITE16();
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        buffer[0] = D1 >> 8; buffer[1] = D1 % (1 << 8);
        
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  C1_buffer = 0;

        find_cache_line();
        Dirty[set][way] = 1;

        Data[set][way][offset] = buffer[0]; Data[set][way][offset + 1] = buffer[1];

        C1_buffer = 7;
        @(negedge clk) C1_buffer = 'z; D1_buffer = 'z;
    endtask

    task C1_WRITE32();
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        buffer[0] = D1 >> 8; buffer[1] = D1 % (1 << 8);
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
                        buffer[2] = D1 >> 8; buffer[3] = D1 % (1 << 8);
        @(negedge clk)  C1_buffer = 0;

        find_cache_line();

        Dirty[set][way] = 1;

        Data[set][way][offset] = buffer[0]; Data[set][way][offset + 1] = buffer[1];
        Data[set][way][offset + 2] = buffer[2]; Data[set][way][offset + 3] = buffer[3];

        C1_buffer = 7;
        @(negedge clk) C1_buffer = 'z; D1_buffer = 'z;
    endtask

    task RESET_TASK();
        for (integer i = 0; i < CACHE_SETS_COUNT; i += 1) begin
            Displacing         [i]    = 0;
            for (integer j = 0; j < CACHE_WAY; j += 1) begin
                Valid          [i][j] = 0;
                Dirty          [i][j] = 0;
                Tag            [i][j] = 0;
                for (integer k = 0; k < CACHE_LINE_SIZE; k += 1)
                    Data[i][j][k] = 0;
            end
        end
    endtask

    task C1_INVALIDATE_LINE();
        tag = A1 >> CACHE_SET_SIZE;
        set = A1 % (1 << CACHE_SET_SIZE);
        @(posedge clk)  offset = A1 % (1 << CACHE_OFFSET_SIZE);
        @(negedge clk)  C1_buffer = 0;

        cache_hit = 0;
        for (integer __way = 0; __way < CACHE_WAY; ++__way) begin
            if (Valid[set][__way] == 1 && Tag[set][__way] == tag && cache_hit == 0) begin
                cache_hit = 1; way = __way;
                if (Dirty[set][way] == 1) begin
                    C2_WRITELINE(); // вытесняем при надобности
                end
                repeat (CACHE_HIT_TIME) @(negedge clk);
            end
        end

        if (cache_hit == 0) begin
            repeat (CACHE_MISS_TIME) @(negedge clk);
        end

        Valid[set][way] = 0;
        C1_buffer = 7;
        @(negedge clk) C1_buffer = 'z;
    endtask

    // ===========МЕТОДЫ ДЛЯ ОБЩЕНИЯ С ПРОЦЕССОРОМ=============
    // ===================КЭШ ЛИНИИ============================
    reg                         Valid       [CACHE_SETS_COUNT-1:0][CACHE_WAY-1:0];
    reg                         Dirty       [CACHE_SETS_COUNT-1:0][CACHE_WAY-1:0];
    reg[CACHE_TAG_SIZE-1:0]     Tag         [CACHE_SETS_COUNT-1:0][CACHE_WAY-1:0];
    reg                         Displacing  [CACHE_SETS_COUNT-1:0];
    reg[7:0]                    Data        [CACHE_SETS_COUNT-1:0][CACHE_WAY-1:0][CACHE_LINE_SIZE-1:0];
    // ===================КЭШ ЛИНИИ============================
    //
    integer HITS, MISSES, TOTAL_REQUESTS;

    initial begin
        HITS = 0; MISSES = 0; TOTAL_REQUESTS = 0; 
        D1_buffer = 'z; C1_buffer = 'z; D2_buffer = 'z; 
        A2 = 'z;    C2_buffer = 0; // изначально кэш владеет C2

        buffer[0] = 0; buffer[1] = 0; buffer[2] = 0; buffer[3] = 0;
        RESET_TASK();
    end

    always @(posedge clk) begin
             if (C_DUMP == 1)       begin DUMP();                  end
             if (RESET == 1)        begin RESET_TASK();            end
             if (C1 == 0)           begin ;                        end // C1_NOP
        else if (C1 == 1)           begin C1_READ8();              end
        else if (C1 == 2)           begin C1_READ16();             end
        else if (C1 == 3)           begin C1_READ32();             end
        else if (C1 == 4)           begin C1_INVALIDATE_LINE();    end
        else if (C1 == 5)           begin C1_WRITE8();             end
        else if (C1 == 6)           begin C1_WRITE16();            end
        else if (C1 == 7)           begin C1_WRITE32();            end
    end

    integer file;
    task DUMP();
        file = $fopen("cache_dump.txt", "w");
        for (integer set_ind = 0; set_ind < CACHE_SETS_COUNT; set_ind += 1) begin
            for (integer set_way = 0; set_way < CACHE_WAY; set_way += 1) begin
                $fwrite(file, "SET %x WAY %x || Valid %1x Dirty %1x Tag %x Data ",
                                set_ind, set_way, Valid[set_ind][set_way], Dirty[set_ind][set_way],
                                Tag[set_ind][set_way]);
                for (integer cur_byte = 0; cur_byte < CACHE_LINE_SIZE; cur_byte += 1) begin
                    $fwrite(file, "%x", Data[set_ind][set_way][cur_byte]);
                end
                $fdisplay(file);
            end
        end
        $fclose(file);
    endtask
endmodule