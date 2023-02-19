`ifndef MEMORY_H
`define MEMORY_H

`include "constants.sv"

module Memory(
    inout wire [16-1:0] D2,
    inout wire [2-1:0] C2,
    input wire [CACHE_TAG_SIZE + CACHE_SET_SIZE-1:0] A2,
    input wire clk,
    input wire RESET,
    input wire M_DUMP 
);

reg[8-1:0] data[MEM_SIZE-1:0]; // непосредственно память

// =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======
reg[16-1:0] D2_buffer; reg[2-1:0] C2_buffer;
assign D2 = D2_buffer; assign C2 = C2_buffer;
// =======РЕГИСТРЫ ДЛЯ ОБЩЕНИЯ=======

initial begin
    C2_buffer = 'z; D2_buffer = 'z;
    RESET_TASK();
end

// =======МЕТОДЫ ДЛЯ ОБЩЕНИЯ=========
reg[CACHE_TAG_SIZE + CACHE_OFFSET_SIZE-1:0] address;
task C2_READLINE(); 
    address = A2;
    @(negedge clk); C2_buffer = 0; // забираем управление
    repeat (MEM_ACCESS_SPEED_READ - 1) @(negedge clk); // вычитаем один такт, так как потратили уже один такт на перехват управления

    C2_buffer = 1;  // C2_RESPONSE
    for (integer i = 0; i < CACHE_LINE_SIZE; i += 2) begin
        D2_buffer = {data[address + i], data[address + i + 1]}; 
        @(negedge clk);
    end
    D2_buffer = 'z; C2_buffer = 'z; // передаем управление
endtask

task C2_WRITELINE();
    address = A2;  
    for (integer i = 0; i < CACHE_LINE_SIZE; i += 2) begin
        data[address + i] = (D2 >> 8);
        data[address + i + 1] = D2 % (1 << 8);
        @(negedge clk);
        if (i == CACHE_LINE_SIZE - 2) begin // перехватываем управление
            C2_buffer = 0;
        end
        @(posedge clk);
    end
    repeat (MEM_ACCESS_SPEED_WRITE) begin @(negedge clk); end
    C2_buffer = 1; // C2_RESPONSE
    @(negedge clk)   C2_buffer = 'z; // передаем управление
endtask
// =======МЕТОДЫ ДЛЯ ОБЩЕНИЯ=========

integer SEED;
task RESET_TASK();
    SEED = _SEED;
    for (int i = 0; i < MEM_SIZE; i++) begin
        data[i] = $random(SEED)>>16; 
    end
endtask

always @(posedge clk) begin
         if (M_DUMP == 1)   begin DUMP();          end
         if (RESET == 1)    begin RESET_TASK();    end
         if (C2 == 0)       begin      ;           end
    else if (C2 == 2)       begin C2_READLINE();   end 
    else if (C2 == 3)       begin C2_WRITELINE();  end
end

integer file;
task DUMP();
    file = $fopen("memory_dump.txt", "w");
    for (integer cur_line = 0; cur_line < MEM_SIZE / CACHE_LINE_SIZE; cur_line += 1) begin
        $fwrite(file, "ADDR: %x || ", (cur_line << CACHE_OFFSET_SIZE));
        for (integer cur_byte = 0; cur_byte < CACHE_LINE_SIZE; cur_byte += 1) begin
            $fwrite(file, "%x", data[(cur_line << CACHE_OFFSET_SIZE) + cur_byte]);
        end
        $fdisplay(file);
    end
    $fclose(file);
endtask

endmodule
`endif