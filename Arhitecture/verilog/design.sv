/*module not_switch(output wire out, input in);
   //wire in;
    supply1 PWR; // питание
    supply0 GRD; // земля

    pmos p1(out, PWR, in); // pmos(drain, source, gate)
    nmos n1(out, GRD, in);


endmodule 
*/

module nand2(output wire out, intput wire in1, in2);
    wire w;
    supply1 PWR;
    supply0 GRD;

    pmos(in, PWR, out);
endmodule