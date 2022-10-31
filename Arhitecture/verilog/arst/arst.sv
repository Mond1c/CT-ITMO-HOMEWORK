module async_rs_trigger(output reg q, q_inv, input wire r, s);
    always @(r or s) begin 
        case ({r,s})
            2'b01: begin
                q = 1'b1;
            end
            2'b10: begin
                q = 1'b0;
            end
            2'b11: begin
                q = 1'bx;
            end
        endcase
        q_inv = ~q;
    end
    
endmodule


