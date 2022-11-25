package test;
    class CPU;
        int number;

        function new(int number);
            this.number = number;
        endfunction


        function void display();
            $display("This is CPU %d", number);
        endfunction
    endclass

    typedef struct packed {
        int first;
        int second;
    } pair;
endpackage