package utility;
    // Start parameters
    parameter MEM_SIZE = 1048576;
    parameter CACHE_SIZE = 16384;
    parameter CACHE_LINE_SIZE = 128;
    parameter CACHE_LINE_COUNT = 128;
    parameter CACHE_WAY = 8;
    parameter CACHE_SETS_COUNT = 16;
    parameter CACHE_TAG_SIZE = 8;
    parameter CACHE_SET_SIZE = 4;
    parameter CACHE_OFFSET_SIZE = 7;
    parameter CACHE_ADDR_SIZE = 20;

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


    //Commands (enums)
    enum {
        C1_NOP,
        C1_READ8,
        C1_READ16,
        C1_READ32,
        C1_INVALIDATE_LINE,
        C1_WRITE8,
        C1_WRITE16,
        C1_WRITE32
    } cpu_cache_commands;

    enum {
        C1_RESPONSE=7
    } cache_cpu_commands;

    enum {
        C2_NOP=0,
        C2_READ_LINE=2,
        C2_WRITE_LINE=3
    } cache_mem_commands;

    enum {
        C2_RESPONSE
    } mem_cache_memory;
endpackage