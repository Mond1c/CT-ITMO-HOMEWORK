`include "mem_ctr.sv"

module cache #(
	parameter MEM_SIZE          = 2097152,
    parameter CACHE_SIZE        = 16384,
    parameter CACHE_LINE_SIZE   = 128,
    parameter CACHE_LINE_COUNT  = 128,
    parameter CACHE_WAY         = 2,
    parameter CACHE_SETS_COUNT  = 64,
    parameter CACHE_TAG_SIZE    = 8,
    parameter CACHE_SET_SIZE    = 6,
    parameter CACHE_OFFSET_SIZE = 7,
    parameter CACHE_ADDR_SIZE   = 21,

    // Start parameters (bits) (bus)
    parameter DATA1_BUS_SIZE    = 16,
    parameter DATA2_BUS_SIZE    = 16,
    parameter ADDR1_BUS_SIZE    = 14,
    parameter ADDR2_BUS_SIZE    = 14,
    parameter CTR1_BUS_SIZE     = 3, // commands from 0 to 7 2^3 = 8
    parameter CTR2_BUS_SIZE     = 2, // commands from 0 to 3 2^2 = 4

    // Commands
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
    input   [ADDR1_BUS_SIZE-1:0]    A1,
    inout   [DATA1_BUS_SIZE-1:0]    D1,
    inout   [CTR1_BUS_SIZE-1:0]     C1
);
	inout 	wire[CTR2_BUS_SIZE-1:0] 	C2;
	output 	wire[ADDR2_BUS_SIZE-1:0] 	A2;
	inout 	wire[DATA2_BUS_SIZE-1:0] 	D2;

	mem_ctr ctr(CLK, RESET, A2, D2, C2);


	bit 						valid[CACHE_LINE_COUNT];
	bit 						dirty[CACHE_LINE_COUNT];
	reg[CACHE_TAG_SIZE-1:0] 	tag[CACHE_LINE_COUNT];
	reg[CACHE_LINE_SIZE-1:0] 	data[CACHE_LINE_COUNT];
	reg[CACHE_SET_SIZE-1:0] 	set[CACHE_LINE_COUNT];
	reg[CACHE_OFFSET_SIZE-1:0] 	offset[CACHE_LINE_COUNT];


	reg[CACHE_TAG_SIZE-1:0] 	addr_tag;
	reg[CACHE_SET_SIZE-1:0] 	addr_set;
	reg[CACHE_OFFSET_SIZE-1:0] 	addr_offset;

	byte command_C1;
	byte command_C2;

	byte need_to_read_D1;
	byte need_to_read_D2=CACHE_LINE_SIZE;

	//Test variables TODO: Need to delete in finally version
	reg[CTR1_BUS_SIZE-1:0] 		C1_test_value = C1_READ8;
	reg[CACHE_TAG_SIZE-1:0] 	tag_test_value = 1;
	reg[CACHE_SET_SIZE-1:0] 	set_test_value = 1;
	reg[CACHE_OFFSET_SIZE-1:0] 	offset_test_value = 2;
	reg 						CLK_test_value = 1;


	reg[DATA1_BUS_SIZE-1:0] d1;
	reg[DATA2_BUS_SIZE-1:0] d2;
	reg[CTR1_BUS_SIZE-1:0] 	c1;
	reg[CTR2_BUS_SIZE-1:0]  c2;
	reg[ADDR2_BUS_SIZE-1:0] a2;
	
	reg C1_write_enabled=0;
	reg D1_write_enabled=0;

	assign C1=(C1_write_enabled) ? c1 : 'bz;
	assign D1=(D1_write_enabled) ? d1 : 'bz;

	assign C2=c2;
	assign D2=d2;

    //I need to create int (maybe byte now idk) array for lru priority
    // Now for debug I use int type
    int lru_priority[CACHE_LINE_COUNT];

	//Logic

	initial begin // test
		//$monitor("#%t Cache: addr_tag=%0b, addr_set=%0b, addr_offset=%0b", $time(), addr_tag, addr_set, addr_offset);
		valid[0] = 1;
		tag[0] = 1;
		set[0] = 1;
		offset[0] = 2;
		data[0] = 835093854354743893;
	end

	always @(posedge CLK or posedge RESET) begin
		C1_write_enabled = 0;
		D1_write_enabled = 0;
		//$display("%0d %0d", $time(), C1);
		if (C1 != 0) begin
			command_C1 = C1;
			addr_tag = A1[CACHE_TAG_SIZE-1:0];
			addr_set = A1[ADDR1_BUS_SIZE-1:CACHE_TAG_SIZE];
			#1 addr_offset = A1[CACHE_OFFSET_SIZE:0];
			#5 C1_write_enabled = 1;
			c1 = C1_RESPONSE;
			case (command_C1)
				C1_READ8: begin
					D1_write_enabled = 1;
					if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
						|| valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
						|| valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
						|| valid[CACHE_WAY * addr_set] == 1 && valid[CACHE_WAY * addr_set + 1] == 1
						&& tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
						$display("Go to memory");
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#1 a2[6:0] = addr_offset;
						valid[CACHE_WAY * addr_set] = 1;
					end
					else begin
						$display("Cache hit!");
					end
					if (valid[CACHE_WAY * addr_set] == 1 && tag[CACHE_WAY * addr_set] == addr_tag) begin // It's working now
						d1 = data[CACHE_WAY * addr_set][addr_offset +:8];
                        lru_priority[CACHE_WAY * addr_set]++;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 1 && tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set + 1][addr_offset +:8];
                        lru_priority[CACHE_WAY * addr_set + 1]++;
					end
				end
				C1_READ16: begin
					if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
						|| valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
						|| valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
						|| valid[CACHE_WAY * addr_set] == 1 && valid[CACHE_WAY * addr_set + 1] == 1
						&& tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
						$display("Go to memory");
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#1 a2[6:0] = addr_offset;
						valid[CACHE_WAY * addr_set] = 1;
					end
					else begin
						$display("Cache hit!");
					end
					if (valid[CACHE_WAY * addr_set] == 1 && tag[CACHE_WAY * addr_set] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set][addr_offset +:16];
                        lru_priority[CACHE_WAY * addr_set]++;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 1 && tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set + 1][addr_offset +:16];
                        lru_priority[CACHE_WAY * addr_set + 1]++;
					end
				end
				C1_READ32: begin
					if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
						|| valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
						|| valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
						|| valid[CACHE_WAY * addr_set] == 1 && valid[CACHE_WAY * addr_set + 1] == 1
						&& tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
						$display("Go to memory");
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#1 a2[6:0] = addr_offset;
						valid[CACHE_WAY * addr_set] = 1;
					end
					else begin
						$display("Cache hit!");
					end
					if (valid[CACHE_WAY * addr_set] == 1 && tag[CACHE_WAY * addr_set] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set][addr_offset +:DATA1_BUS_SIZE];
						#1 d1 = data[CACHE_WAY * addr_set][addr_offset+16 +:DATA1_BUS_SIZE];
                        lru_priority[CACHE_WAY * addr_set]++;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 1 && tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set + 1][addr_offset +:DATA1_BUS_SIZE];
						#1 d1 = data[CACHE_WAY * addr_set + 1][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE];
						lru_priority[CACHE_WAY * addr_set + 1]++;
					end
				end
				C1_INVALIDATE_LINE: begin
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin
						data[CACHE_WAY * addr_set] = 0;
						dirty[CACHE_WAY * addr_set] = 1;
					end	
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						data[CACHE_WAY * addr_set + 2] = 0;
						dirty[CACHE_WAY * addr_set + 1] = 1;
					end
				end
				C1_WRITE8: begin
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin
						data[CACHE_WAY * addr_set][addr_offset +:8] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
					end
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						data[CACHE_WAY * addr_set + 1][addr_offset +:8] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
					end
				end
				C1_WRITE16: begin
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin
						data[CACHE_WAY * addr_set][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
					end
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						data[CACHE_WAY * addr_set + 1][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
					end
				end
				C1_WRITE32: begin
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin
						data[CACHE_WAY * addr_set][addr_offset +:DATA1_BUS_SIZE] = D1;
						#1 data[CACHE_WAY * addr_set][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
					end
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						data[CACHE_WAY * addr_set + 1][addr_offset +:DATA1_BUS_SIZE] = D1;
						#1 data[CACHE_WAY * addr_set + 1][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
					end
				end
			endcase

		end
		else begin
			c1 = C1_NOP;
		end
	end


	function byte get_data_size(byte command);
		case(command)
			C1_READ8: return 8;
			C1_READ16: return 16;
			C1_READ32: return 32;
			C1_WRITE8: return 8;
			C1_WRITE16: return 16;
			C1_WRITE32: return 32;
		endcase
		return 0;
	endfunction

endmodule : cache
