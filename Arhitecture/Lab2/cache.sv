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


	reg[CACHE_TAG_SIZE-1:0] 	addr_tag;
	reg[CACHE_SET_SIZE-1:0] 	addr_set;
	reg[CACHE_OFFSET_SIZE-1:0] 	addr_offset;

	byte command_C1;

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
	
	bit C1_write_enabled = 0;
	bit D1_write_enabled = 0;
	bit command_is_running = 0;

	assign C1 = (C1_write_enabled) ? c1 : 'bz;
	assign D1 = (D1_write_enabled) ? d1 : 'bz;

	assign C2 = c2;
	assign D2 = d2;
	assign A2 = a2;

    //I need to create int (maybe byte now idk) array for lru priority
    // Now for debug I use int type
    int lru_priority[CACHE_LINE_COUNT];

	//Logic

	function void reset();
		for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
			valid[i] = 0;
			dirty[i] = 0;
			tag[i] = 0;
			data[i] = 0;
		end
		addr_tag = 0;
		addr_set = 0;
		addr_offset = 0;
		command_C1 = 0;
		command_is_running = 0;
	endfunction

	initial begin // test
		d2 = 'bz;
		//$monitor("time = %0d, D2 = %b", $time(), D2);
		//$monitor("C2=$0d", C2);
		//$monitor("#%t Cache: addr_tag=%0b, addr_set=%0b, addr_offset=%0b", $time(), addr_tag, addr_set, addr_offset);
		//valid[0] = 1;
		//tag[0] = 1;
		//set[0] = 1;
		//offset[0] = 2;
		//data[0] = 835093854354743893;
	end

	function byte get_data_size(byte command);
		case(command)
			C1_READ8: return 8;
			C1_READ16: return 16;
			C1_READ32: return 32;
		endcase
		return 0;
	endfunction

	task read_data_from_mem;
		#8 $display("Cache miss!"); // Check task document (about 4 ticks)
		$display("valid = %d", valid[CACHE_WAY * addr_set]);
		if (valid[CACHE_WAY * addr_set] == 0) begin
			c2 = C2_READ_LINE;
			a2[7:0] = addr_tag;
			a2[13:8] = addr_set;
			d2 = 'bz;
			#200 valid[CACHE_WAY * addr_set] = 1;
			tag[CACHE_WAY * addr_set] = addr_tag;
			for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
				#(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +:DATA2_BUS_SIZE] = D2; 
			end
		end
		else if (valid[CACHE_WAY * addr_set + 1] == 0) begin
			c2 = C2_READ_LINE;
			a2[7:0] = addr_tag;
			a2[13:8] = addr_set;
			d2 = 'bz;
			#200 valid[CACHE_WAY * addr_set + 1] = 1;
			tag[CACHE_WAY * addr_set + 1] = addr_tag;
			for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
				#(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +:DATA2_BUS_SIZE] = D2;
			end
		end
		else if (lru_priority[CACHE_WAY * addr_set] < lru_priority[CACHE_WAY * addr_set + 1]) begin
			if (dirty[CACHE_WAY * addr_set] == 1) begin
				c2 = C2_WRITE_LINE;
				a2[7:0] = addr_tag;
				a2[13:8] = addr_set;
				#200 dirty[CACHE_WAY * addr_set] = 1; // Write in the main memory
			end
				c2 = C2_READ_LINE;
				a2[7:0] = addr_tag;
				a2[13:8] = addr_set;
				d2 = 'bz;
				#200 valid[CACHE_WAY * addr_set] = 1;
				tag[CACHE_WAY * addr_set + 1] = addr_tag;
				for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
					#(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set][i +:DATA2_BUS_SIZE] = D2; 
				end
				lru_priority[CACHE_WAY * addr_set] = 0;
			end
			else begin
				if (dirty[CACHE_WAY * addr_set + 1] == 1) begin
					c2 = C2_WRITE_LINE;
					a2[7:0] = addr_tag;
					a2[13:8] = addr_set;
					#200 dirty[CACHE_WAY * addr_set + 1] = 1; // Write in the main memory
				end
				c2 = C2_READ_LINE;
				a2[7:0] = addr_tag;
				a2[13:8] = addr_set;
				#200 valid[CACHE_WAY * addr_set + 1] = 1;
				tag[CACHE_WAY * addr_set + 1] = 1;
				for (int i = 0; i < CACHE_LINE_SIZE; i += DATA2_BUS_SIZE) begin
					#(i > 0 ? 2 : 0) data[CACHE_WAY * addr_set + 1][i +:DATA2_BUS_SIZE] = D2;
				end
				lru_priority[CACHE_WAY * addr_set + 1] = 0;
			end
	endtask


	always @(posedge CLK or posedge RESET) begin
		if (RESET) begin
			reset();
		end
		C1_write_enabled = 0;
		D1_write_enabled = 0;
		//$display("%0d %0d", $time(), C1);
		$display("time=%d, C1=%d", $time(), C1);
		if (C1 != 0 && command_is_running == 0) begin
			command_is_running = 1;
			command_C1 = C1;
			addr_tag = A1[CACHE_TAG_SIZE-1:0];
			addr_set = A1[ADDR1_BUS_SIZE-1:CACHE_TAG_SIZE];
			#2 addr_offset = A1[CACHE_OFFSET_SIZE:0];
		//	#5 C1_write_enabled = 1;
	//	c1 = C1_RESPONSE;
			//$display("C1= %d", command_C1);
			case (command_C1)
				C1_READ8: begin
					if (valid[CACHE_WAY * addr_set] == 0 && valid[CACHE_WAY * addr_set + 1] == 0
						|| valid[CACHE_WAY * addr_set] == 0 && tag[CACHE_WAY * addr_set + 1] != addr_tag
						|| valid[CACHE_WAY * addr_set + 1] == 0 && tag[CACHE_WAY * addr_set] != addr_tag
						|| valid[CACHE_WAY * addr_set] == 1 && valid[CACHE_WAY * addr_set + 1] == 1
						&& tag[CACHE_WAY * addr_set] != addr_tag && tag[CACHE_WAY * addr_set + 1] != addr_tag) begin
						read_data_from_mem();
					end
					else begin
						#12 $display("Cache hit!"); // if cache hit we need to wait 6 ticks (check task document)
					end
					C1_write_enabled = 1;
					D1_write_enabled = 1;
					c1 = C1_RESPONSE;
					$display("cache line = %b, tag = %b, addr_tag = %b", data[addr_set * CACHE_WAY], tag[CACHE_WAY * addr_set], addr_tag);
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin // It's working now
						d1 = data[CACHE_WAY * addr_set][addr_offset +:8];
                        lru_priority[CACHE_WAY * addr_set]++;
					end
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
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
						read_data_from_mem();
					end
					else begin
						#12 $display("Cache hit!");
					end
					C1_write_enabled = 1;
					c1 = C1_RESPONSE;
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
						read_data_from_mem();
					end
					else begin
						#12 $display("Cache hit!");
					end
					C1_write_enabled = 1;
					c1 = C1_RESPONSE;
					if (valid[CACHE_WAY * addr_set] == 1 && tag[CACHE_WAY * addr_set] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set][addr_offset +:DATA1_BUS_SIZE];
						#2 d1 = data[CACHE_WAY * addr_set][addr_offset+16 +:DATA1_BUS_SIZE];
                        lru_priority[CACHE_WAY * addr_set]++;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 1 && tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						d1 = data[CACHE_WAY * addr_set + 1][addr_offset +:DATA1_BUS_SIZE];
						#2 d1 = data[CACHE_WAY * addr_set + 1][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE];
						lru_priority[CACHE_WAY * addr_set + 1]++;
					end
				end
				C1_INVALIDATE_LINE: begin
					//$display("123");
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = addr_tag;
							a2[13:8] = addr_set;
						end
						data[CACHE_WAY * addr_set] = 0;
						dirty[CACHE_WAY * addr_set] = 0;
						valid[CACHE_WAY * addr_set] = 0;
					end	
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						if (dirty[CACHE_WAY * addr_set + 1] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = addr_tag;
							a2[13:8] = addr_set;
						end
						data[CACHE_WAY * addr_set + 1] = 0;
						dirty[CACHE_WAY * addr_set + 1] = 0;
						valid[CACHE_WAY * addr_set] = 0;
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
					else if (valid[CACHE_WAY * addr_set] == 0) begin
						data[CACHE_WAY * addr_set][addr_offset +:8] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 0) begin
						data[CACHE_WAY * addr_set + 1][addr_offset +:8] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else if (lru_priority[CACHE_WAY * addr_set] < lru_priority[CACHE_WAY * addr_set + 1]) begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = tag[CACHE_WAY * addr_set];
							a2[13:8] = addr_set;
							#200 $display("Write back done!");
						end
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#200 data[CACHE_WAY * addr_set][addr_offset +:8] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = tag[CACHE_WAY * addr_set + 1];
							a2[13:8] = addr_set;
							#200 $display("Write back done!");
						end
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#200 data[CACHE_WAY * addr_set + 1][addr_offset +:8] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
						valid[CACHE_WAY * addr_set + 1] = 1;
						tag[CACHE_WAY * addr_set + 1] = addr_tag;
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
					else if (valid[CACHE_WAY * addr_set] == 0) begin
						data[CACHE_WAY * addr_set][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 0) begin
						data[CACHE_WAY * addr_set][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = 1;
					end
					else if (lru_priority[CACHE_WAY * addr_set] < lru_priority[CACHE_WAY * addr_set + 1]) begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = tag[CACHE_WAY * addr_set];
							a2[13:8] = addr_set;
							#100 $display("Write back done!");
						end
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#200 data[CACHE_WAY * addr_set][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = tag[CACHE_WAY * addr_set + 1];
							a2[13:8] = addr_set;
							#200 $display("Write back done!");
						end
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#200 data[CACHE_WAY * addr_set + 1][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
						valid[CACHE_WAY * addr_set + 1] = 1;
						tag[CACHE_WAY * addr_set + 1] = addr_tag;
					end
				end
				C1_WRITE32: begin
					if (tag[CACHE_WAY * addr_set] == addr_tag) begin
						data[CACHE_WAY * addr_set][addr_offset +:DATA1_BUS_SIZE] = D1;
						#2 data[CACHE_WAY * addr_set][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
					end
					else if (tag[CACHE_WAY * addr_set + 1] == addr_tag) begin
						data[CACHE_WAY * addr_set + 1][addr_offset +:DATA1_BUS_SIZE] = D1;
						#2 data[CACHE_WAY * addr_set + 1][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
					end
					else if (valid[CACHE_WAY * addr_set] == 0) begin
						data[CACHE_WAY * addr_set][addr_offset +:DATA1_BUS_SIZE] = D1;
						#2 data[CACHE_WAY * addr_set][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE] = D1;
						valid[CACHE_WAY * addr_set] = 1;
						dirty[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else if (valid[CACHE_WAY * addr_set + 1] == 0) begin
						data[CACHE_WAY * addr_set + 1][addr_offset +:DATA1_BUS_SIZE] = D1;
						#2 data[CACHE_WAY * addr_set + 1][addr_offset+DATA1_BUS_SIZE +:DATA1_BUS_SIZE] = D1;
						valid[CACHE_WAY * addr_set + 1] = 1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
						tag[CACHE_WAY * addr_set + 1] = addr_tag;
					end
					else if (lru_priority[CACHE_WAY * addr_set] < lru_priority[CACHE_WAY * addr_set + 1]) begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = tag[CACHE_WAY * addr_set];
							a2[13:8] = addr_set;
							#200 $display("Write back done!");
						end
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#200 data[CACHE_WAY * addr_set][addr_offset +:16] = D1;
						#2 data[CACHE_WAY * addr_set][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set] = 1;
						valid[CACHE_WAY * addr_set] = 1;
						tag[CACHE_WAY * addr_set] = addr_tag;
					end
					else begin
						if (dirty[CACHE_WAY * addr_set] == 1) begin
							$display("Write back");
							c2 = C2_WRITE_LINE;
							a2[7:0] = tag[CACHE_WAY * addr_set + 1];
							a2[13:8] = addr_set;
							#2 a2[6:0] = addr_offset;
							#200 $display("Write back done!");
						end
						c2 = C2_READ_LINE;
						a2[7:0] = addr_tag;
						a2[13:8] = addr_set;
						#200 data[CACHE_WAY * addr_set + 1][addr_offset +:16] = D1;
						#2 data[CACHE_WAY * addr_set + 1][addr_offset +:16] = D1;
						dirty[CACHE_WAY * addr_set + 1] = 1;
						valid[CACHE_WAY * addr_set + 1] = 1;
						tag[CACHE_WAY * addr_set + 1] = addr_tag;
					end
				end
			endcase
			command_is_running = 0;
		end
		else begin
			c1 = C1_NOP;
		end
	end


endmodule : cache
