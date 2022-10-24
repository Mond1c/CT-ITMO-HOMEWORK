/*module not_switch(output wire out, input in);
   //wire in;
    supply1 PWR; // питание
    supply0 GRD; // земля

    pmos p1(out, PWR, in); // pmos(drain, source, gate)
    nmos n1(out, GRD, in);


endmodule 
*/

module nand2(output out, input in1, in2);
    wire w;
    supply1 PWR;
    supply0 GRD;

    pmos p1(out, PWR, in1);
    pmos p2(out, PWR, in2);
    nmos n1(out, w, in1);
    nmos n2(w, GRD, in2);
endmodule
