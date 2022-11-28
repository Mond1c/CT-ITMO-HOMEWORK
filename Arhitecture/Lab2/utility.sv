package utility;
    parameter CACHE_LINE_SIZE   = 128;
    parameter CACHE_TAG_SIZE    = 8;
    parameter CACHE_SET_SIZE    = 6;
    parameter CACHE_OFFSET_SIZE = 7;

    typedef struct packed {
        bit valid;
        bit dirty;
        reg[CACHE_TAG_SIZE] tag;
        bit data[CACHE_LINE_SIZE];
    } cache_line_t;

    typedef struct packed {
        reg[CACHE_TAG_SIZE] tag;
        reg[CACHE_SET_SIZE] set;
        reg[CACHE_OFFSET_SIZE] offset;
    } cache_address_t;

endpackage
