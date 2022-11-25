module cache_sv();
    parameter MEM_SIZE = 2097152;
    parameter CACHE_SIZE = 16384;
    parameter CACHE_LINE_SIZE = 128;
    parameter CACHE_LINE_COUNT = 128;
    parameter CACHE_WAY = 2;
    parameter CACHE_SETS_COUNT = 64;
    parameter CACHE_TAG_SIZE = 8;
    parameter CACHE_SET_SIZE = 6;
    parameter CACHE_OFFSET_SIZE = 7;
    parameter CACHE_ADDR_SIZE = 21;

    typedef struct packed {
        bit valid;
        bit dirty;
        bit[CACHE_TAG_SIZE] tag;
        bit[CACHE_LINE_SIZE] data;
    } cache_line;

    typedef struct packed {
        bit[CACHE_TAG_SIZE] tag;
        bit[CACHE_SET_SIZE] set;
        bit[CACHE_OFFSET_SIZE] offset;
    } cache_address;

    
endmodule