package utility;
    // Start parameters (bits) (cache)
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

    // Start parameters (bits) (bus)
    parameter DATA1_BUS_SIZE = 16;
    parameter DATA2_BUS_SIZE = 16;
    parameter ADDR1_BUS_SIZE = 14;
    parameter ADDR2_BUS_SIZE = 14;
    parameter CTR1_BUS_SIZE = 3; // commands from 0 to 7 2^3 = 8
    parameter CTR2_BUS_SIZE = 2; // commands from 0 to 3 2^2 = 4

    typedef struct packed {
        bit valid;
        bit dirty;
        reg[CACHE_TAG_SIZE] tag;
        reg[CACHE_LINE_SIZE] data;
    } cache_line;

    typedef struct packed {
        reg[CACHE_TAG_SIZE] tag;
        reg[CACHE_SET_SIZE] set;
        reg[CACHE_OFFSET_SIZE] offset;
    } cache_address;
endpackage