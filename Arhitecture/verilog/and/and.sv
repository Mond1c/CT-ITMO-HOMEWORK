module and2(output out, input in1, in2);
    wire w;
    supply0 GRD;
    supply1 PWR;

    nmos n1(w, PWR, in1);
    nmos n2(out, w, in2);
    
    pmos p1(out, GRD, in1);
    pmos p2(out, GRD, in2);
endmodule
