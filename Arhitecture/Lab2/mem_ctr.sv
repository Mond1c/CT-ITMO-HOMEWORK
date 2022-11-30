module mem_ctr #(
	parameter MEM_SIZE		 = 2097152,
	parameter ADDR2_BUS_SIZE = 14,
	parameter DATA2_BUS_SIZE = 16,
	parameter CTR2_BUS_SIZE  = 2
) (
	input 							CLK,
	input 							RESET,  
	input wire[ADDR2_BUS_SIZE-1:0]	A2,
	inout wire[DATA2_BUS_SIZE-1:0]	D2,
	inout wire[CTR2_BUS_SIZE-1:0]	C2
);

	//reg DATA1_BUS_SIZE

endmodule : mem_ctr