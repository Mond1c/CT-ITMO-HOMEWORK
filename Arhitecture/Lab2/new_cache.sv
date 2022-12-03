`include "mem_ctr.sv"

module new_cache #(
    parameter MEM_SIZE              = 2097152,
    parameter CACHE_SIZE            = 16384,
    parameter CACHE_LINE_SIZE       = 128,
    parameter CACHE_WAY             = 2,
    parameter CACHE_SETS_COUNT      = 64,
    parameter CACHE_TAG_SIZE        = 8,
    parameter CACHE_SET_SIZE        = 6,
    parameter CACHE_OFFSET_SIZE     = 7,
    parameter CACHE_ADDR_SIZE       = 21,

    parameter DATA1_BUS_SIZE        = 16,
    parameter DATA2_BUS_SIZE        = 16,
    parameter ADDR1_BUS_SIZE        = 14,
    parameter ADDR2_BUS_SIZE        = 14,
    parameter CTR1_BUS_SIZE         = 3,
    parameter CTR2_BUS_SIZE         = 2,

    parameter C1_NOP                = 0,
    parameter C1_READ8              = 1,
    parameter C1_READ16             = 2,
    parameter C1_READ32             = 3,
    parameter C1_INVALIDATE_LINE    = 4,
    parameter C1_WRITE8             = 5,
    parameter C1_WRITE16            = 6,
    parameter C1_WRITE32            = 7,
    parameter C1_RESPONSE           = 7,
    parameter C2_NOP                = 0,
    parameter C2_READ_LINE          = 2,
    parameter C2_WRITE_LINE         = 3,
    parameter C2_RESPONSE           = 1
    ) (
    input                           CLK,
    input                           RESET,
    input wire[ADDR1_BUS_SIZE-1:0]  A1,
    inout wire[DATA1_BUS_SIZE-1:0]  D2,
    inout wire[CTR1_BUS_SIZE-1:0]   C1
    );
    output  wire[ADDR2_BUS_SIZE-1:0]    A2;
    inout   wire[DATA2_BUS_SIZE-1:0]    D2;
    inout   wire[CTR2_BUS_SIZE-1:0]     C2;

    mem_ctr ctr(CLK, RESET, A2, D2, C2);

    bit valid[CACHE_LINE_COUNT];
    bit dirty[CACHE_LINE_COUNT];
    reg[CACHE_TAG_SIZE-1:0] tag[CACHE_LINE_COUNT];
    reg[CACHE_LINE_SIZE-1:0] data[CACHE_LINE_COUNT];

    reg[CACHE_TAG_SIZE-1:0] addr_tag;
    reg[CACHE_SET_SIZE-1:0] addr_set;
    reg[CACHE_OFFSET_SIZE-1:0] addr_offset;

    byte cur_command;

    reg[DATA1_BUS_SIZE-1:0] d1;
    reg[DATA2_BUS_SIZE-1:0] d2;
    reg[ADDR2_BUS_SIZE-1:0] a2;
    reg[CTR1_BUS_SIZE-1:0]  c1;
    reg[CTR2_BUS_SIZE-1:0]  c2;

    bit C1_write_enabled = 0;
    bit D1_write_enabled = 0;
    bit command_is_running = 0;

    assign C1 = (C1_write_enabled) ? c1 : 'bz;
    assign D1 = (D1_write_enabled) ? d2 : 'bz;

    assign C2 = c2;
    assign D2 = d2;
    assign A2 = a2;

    int lru_priority[CACHE_LINE_COUNT];

    int cache_hits_count = 0;
    int cache_miss_count = 0;


    function cache_info();
        $display("Requets = %d", cache_miss_count + cache_hits_count);
        $display("Cache hits = %d", cache_hits_count);
        $display("Cache misses = %d", cache_miss_count);
        $display("Cache hit ratio = %f", (1.0 * cache_hits_count) / (1.0 * (cache_hits_count + cache_miss_count)));
    endfunction

    function reset();
        for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
            valid[i] = 0;
            dirty[i] = 0;
            tag[i] = 0;
            data[i] = 0;
        end
        addr_tag = 'bz;
        addr_set = 'bz;
        addr_offset = 'bz;
        cur_command = 0;
        command_is_running = 0;
        cache_hits_count = 0;
        cache_miss_count = 0;
    endfunction

    function byte get_data_size(byte command);
        case(command)
            C1_READ8: return 8;
            C1_READ16: return 16;
            C1_READ32: return 32;
        endcase
        return 0;
    endfunction

    task read_data_from_mem;
        #8 $display("Cache miss!");
        cache_miss_count++;
    endtask
endmodule : new_cache

