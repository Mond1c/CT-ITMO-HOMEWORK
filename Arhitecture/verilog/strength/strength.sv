module strength(output out, input i1, i2);  
  //assign (supply1, pull0) out = (i1 | i2);
  //assign (strong1, supply0) out = i1 & i2;  
  
  or (supply1, strong0) o1(out, i1, i2); // supply 1 - сила для 1, strong0 - сила для 0
  and (strong1, supply0) a1(out, i1, i2);
endmodule