module xor2(output out, input in1, in2);
    wire w;
    supply1 PWR;
    supply0 GRD;

    pmos p1(w, PWR, in1);
    nmos n1(w, GRD, in1);
    pmos p2(out, in1, in2);
    nmos n2(out, w, in2);
endmodule
