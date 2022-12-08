module mem_ctr #(
	parameter _SEED			 	= 225526,
	parameter MEM_SIZE		 	= 2097152,
	parameter CACHE_LINE_SIZE 	= 16384,
	parameter CACHE_LINE_COUNT	= 128, // in one segment 
	parameter MEM_TAG_SIZE 		= 14, // Count of all cache line equals 16384 = 2^14
	parameter MEM_OFFSET_SIZE 	= 7,
	parameter ADDR2_BUS_SIZE 	= 14,
	parameter DATA2_BUS_SIZE 	= 16,
	parameter CTR2_BUS_SIZE  	= 2,
	parameter MEM_SEGMENT_COUNT	= 128, // MEM_SEGMENT_COUNT = MEM_SIZE / CACHE_SIZE

	parameter C2_NOP			= 0,
	parameter C2_READ_LINE		= 2,
	parameter C2_WRITE_LINE		= 3,
	parameter C2_RESPONSE		= 1
) (
	input 							CLK,
	input 							RESET,  
	input wire[ADDR2_BUS_SIZE-1:0]	A2,
	inout wire[DATA2_BUS_SIZE-1:0]	D2,
	inout wire[CTR2_BUS_SIZE-1:0]	C2
);
	int SEED = 225526;

	reg[DATA2_BUS_SIZE-1:0] d2='bz;
	reg[CTR2_BUS_SIZE-1:0]	c2='bz;

	bit D2_write_enabled = 0;
	bit C2_write_enabled = 0;

	reg[MEM_TAG_SIZE-1:0] 		tag[CACHE_LINE_COUNT];
	bit[CACHE_LINE_SIZE-1:0] 	data[CACHE_LINE_COUNT];

	assign D2 =  d2;
	assign C2 =  c2;

	byte						command;
	reg[MEM_TAG_SIZE-1:0]		addr_tag;

	// program variables
    localparam M = 64;
    localparam N = 60;
    localparam K = 32;
    reg[7:0] a;
    reg[15:0] b;
    reg[31:0] c;

    function void random_init();
		for (int i = 0; i < CACHE_LINE_COUNT * MEM_SEGMENT_COUNT; i++) begin
			data[i] = $random(SEED);
			tag[i] = i;
		end
	endfunction

    function void init();
        int n = 0;
        int m = 0;
        int k = 0;
        for (int i = 0; i < M; i++) begin
            for (int j = 0; j < K; j++) begin
                if (k >= 128) begin
                    m++;
                    k = 0;
                end
                a = $random(SEED);
                data[m][k +: 8] = a;
                k += 8;
            end
        end
        n = 0;
        k = 0;
        m = 0;
        for (int i = 0; i < K; i++) begin
            for (int j = 0; j < N; j++) begin
                if (n >= 128) begin
                    k++;
                    n = 0;
                end
                b = $random(SEED);
                data[k][m +: 16] = b;
                m += 16;
            end
        end
        n = 0;
        k = 0;
        m = 0;
        for (int i = 0; i < M; i++) begin
            for (int j = 0; j < N; j++) begin
                if (n >= 128) begin
                    m++;
                    n = 0;
                end
                c = $random(SEED);
                data[m][n +: 32] = c;
                n += 32;
            end
        end
    endfunction

	initial begin // generate random memory
	//	$monitor("D2 = %b", D2);
		random_init();
	end

	always @(posedge CLK or posedge RESET) begin
		// /$display("%0d", C2);
		//$display("%d", C2);
		C2_write_enabled=0;
		D2_write_enabled=0;
		if (RESET) begin
			init();
		end
		else begin
			command = C2;
			//$display(command);
			case(command)
				C2_READ_LINE: begin // I think it's ok
					addr_tag = A2;
					#200 C2_write_enabled = 1; 
					c2 = C2_RESPONSE;
					#2 c2 = 'bz;
					for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
						if (tag[i] == addr_tag) begin
							D2_write_enabled = 1;
							for (int j = 0; j < CACHE_LINE_SIZE; j += DATA2_BUS_SIZE) begin
								#(j > 0 ? 2 : 0) d2 = data[i][j +:DATA2_BUS_SIZE];
							end
						end
					end
				//	#2 c2 = 'bz;
				end
				C2_WRITE_LINE: begin
					//c2 = C2_RESPONSE;
					addr_tag = A2;
					for (int i = 0; i < CACHE_LINE_COUNT; i++) begin
						if (tag[i] == addr_tag) begin
							for (int j = 0; j < CACHE_LINE_SIZE; j += DATA2_BUS_SIZE) begin
								#(i > 0 ? 2 : 0) data[i][j +: DATA2_BUS_SIZE] = D2;
							end
						end
					end
					#186	c2 = C2_RESPONSE;
				end
			endcase
		end
	end

endmodule : mem_ctr
