`ifndef CONSTANTS_H 
`define CONSTANTS_H
parameter MEM_SIZE = (1 << 18);
parameter CACHE_SIZE = (1 << 11);
parameter CACHE_LINE_SIZE = (1 << 4);
parameter CACHE_LINE_COUNT = (1 << 7);
parameter CACHE_WAY = (2);
parameter CACHE_SETS_COUNT = (1 << 6);
parameter CACHE_TAG_SIZE = (8);
parameter CACHE_SET_SIZE = (6);
parameter CACHE_OFFSET_SIZE = (4);
parameter CACHE_ADDR_SIZE = (18);
parameter _SEED = (225526);

parameter MEM_ACCESS_SPEED_READ = 100; 
parameter MEM_ACCESS_SPEED_WRITE = 100 - CACHE_LINE_SIZE / 2;  // учитываем
parameter CACHE_HIT_TIME   = 6; 
parameter CACHE_MISS_TIME  = 4; 
`endif