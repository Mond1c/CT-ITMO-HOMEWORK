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
    parameter CTR1_BUS_SIZE = 16;
    parameter CTR2_BUS_SIZE = 16;

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

    typedef struct packed {
        bit[DATA1_BUS_SIZE] data1;
        bit[DATA2_BUS_SIZE] data2;
    } data_bus;

    typedef struct packed {
        bit[ADDR1_BUS_SIZE] addr1;
        bit[ADDR2_BUS_SIZE] addr2;
    } addr_bus;

    typedef struct packed {
        bit[CTR1_BUS_SIZE] ctr1;
        bit[CTR2_BUS_SIZE] ctr2;
    } ctr_bus;

    typedef bit[MEM_SIZE] memory;
    typedef cache_line[CACHE_LINE_COUNT] cache;
endpackage