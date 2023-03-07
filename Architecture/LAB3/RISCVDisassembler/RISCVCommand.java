package RISCVDisassembler;

import java.util.Map;

public class RISCVCommand {
    private static final String UNKNOWN = "UNKNOWN COMMAND";

    private int data;
    private int address;

    public RISCVCommand(byte[] data, int address) {
        assert data.length == 4 : "bad command size";
        this.data = base.MyUtil.BytesToInt(data);
        this.address = address;
    }

    public RISCVCommand(int data, int address) {
        this.data = data;
        this.address = address;
    }

    public String disAssembly(Map<Integer, String> symtab) {
        int opcode = getOpcode();
        return switch (opcode) {
            case 0b0110111 -> parseLUI(); 
            case 0b0010111 -> parseAUIPC(); 
            case 0b1101111 -> parseJAL(symtab); 
            case 0b1100111 -> parseJALR(); 
            case 0b1100011 -> parseBEQorBNEorBLTorBGEorBLTUorBGEU(symtab); 
            case 0b0000011 -> parseLBorLHorLWorLBUorLHU(); 
            case 0b0100011 -> parseSBorSHorSW(); 
            case 0b0010011 -> parseADDIorSLTIorSLTIUorXORIorORIorANDIorSLLIorSRLIorSRAI(); 
            case 0b0110011 -> parseADDorSUBorSLLorSLTorSLTUorXORorSRLorSRAorORorANDorMULorMULHorMULHSUorMULHUorDIVorDIVUorREMorREMU(); 
            case 0b0001111 -> parseFENCE(); 
            case 0b1110011 -> parseECALLorEBREAK(); 
            default -> threeArgumentsInstruction(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
        };
    }


    static int idx = 0;

    public void updateLabels(Map<Integer, String> symtab) {
        int dest = switch (getOpcode()) {
            case 0b1101111 -> address + immJ(); // JAL
            case 0b1100011 -> address + immB(); // BEQ BNE BLT BGE BLTU BGEU
            default -> -1; 
        };
        if (dest > -1 && !symtab.containsKey(dest)) {
            symtab.put(dest, String.format("L%d", idx));
            idx++;
        }
    }

    private String parseADDorSUBorSLLorSLTorSLTUorXORorSRLorSRAorORorANDorMULorMULHorMULHSUorMULHUorDIVorDIVUorREMorREMU() {
        String nameOfCommand = UNKNOWN;
        switch (getFunct3()) {
        case 0b000 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "add";
            }
            if (getFunct7() == 0b0100000) {
                nameOfCommand = "sub";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "mul";
            }
        }
        case 0b001 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "sll";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "mulh";
            }
        }
        case 0b010 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "slt";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "mulhsu";
            }
        }
        case 0b011 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "sltu";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "mulhu";
            }
        }
        case 0b100 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "xor";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "div";
            }
        }
        case 0b101 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "srl";
            }
            if (getFunct7() == 0b0100000) {
                nameOfCommand = "sra";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "divu";
            }
        }
        case 0b110 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "or";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "rem";
            }
        }
        case 0b111 -> {
            if (getFunct7() == 0) {
                nameOfCommand = "and";
            }
            if (getFunct7() == 0b0000001) {
                nameOfCommand = "remu";
            }
        }
        }

        return threeArgumentsInstruction(nameOfCommand, getRSRegister(getRD()), getRSRegister(getRS1()),
                getRSRegister(getRS2()));
    }

    private String parseADDIorSLTIorSLTIUorXORIorORIorANDIorSLLIorSRLIorSRAI() {
        String nameOfCommand = UNKNOWN; int immediate = 0;
        switch (getFunct3()) {
            case 0b000 -> {
                nameOfCommand = "addi";
                immediate = immI();
            }
            case 0b001 -> {
                if (getFunct7() == 0) {
                    nameOfCommand = "slli";
                }
                immediate = immI() & 0b11111;
            }
            case 0b010 -> {
                nameOfCommand = "slti";
                immediate = immI();
            }
            case 0b011 -> {
                nameOfCommand = "sltiu";
                immediate = immI();
            }
            case 0b100 -> {
                nameOfCommand = "xori";
                immediate = immI();
            }
            case 0b101 -> {
                if (getFunct7() == 0) {
                    nameOfCommand = "srli";
                }
                if (getFunct7() == 0b0100000) {
                    nameOfCommand = "srai";
                }
                immediate = immI() & 0b11111;
            }
            case 0b110 -> {
                nameOfCommand = "ori";
                immediate = immI();
            }
            case 0b111 -> {
                nameOfCommand = "andi";
                immediate = immI();
            }
        }

        return threeArgumentsInstruction(nameOfCommand, getRSRegister(getRD()), getRSRegister(getRS1()), String.valueOf(immediate));
    }

    private String parseLBorLHorLWorLBUorLHU() {
        String nameOfCommand = UNKNOWN;
        switch (getFunct3()) {
            case 0b000 -> {
                nameOfCommand = "lb";
            }
            case 0b001 -> {
                nameOfCommand = "lh";
            }
            case 0b010 -> {
                nameOfCommand = "lw";
            }
            case 0b100 -> {
                nameOfCommand = "lbu";
            }
            case 0b101 -> {
                nameOfCommand = "lhu";
            }
        }

        return LOADorSTOREorJALRInstruction(nameOfCommand, getRSRegister(getRD()), String.valueOf(immI()), getRSRegister(getRS1()));
    }

    private String parseLUI() {
        return twoArgumentsInstruction("lui", getRSRegister(getRD()), String.valueOf(immU()));
    }

    private String parseAUIPC() {
        return twoArgumentsInstruction("auipc", getRSRegister(getRD()), String.valueOf(immU()));
    }

    private String parseECALLorEBREAK() {
        String name = UNKNOWN;
        if (getRD() == 0 && getFunct3() == 0 && getRS1() == 0 && ((data >>> 20) & 0xfff) == 0) {
            name = "ecall";
        } else if (getRD() == 0 && getFunct3() == 0 && getRS1() == 0 && ((data >>> 20) & 0xfff) == 1) {
            name = "ebreak";
        }
        return noArgumentsInstruction(name);
    }

    private String parseFENCE() {
        String name = UNKNOWN;
        if (getFunct3() == 0) {
            name = "fence";
        }
        return fenceInstruction(name);
    }

    private String parseJALR() {         
        String nameOfCommand = UNKNOWN;
        if (getFunct3() == 0) {
            nameOfCommand = "jalr";
        }

        return LOADorSTOREorJALRInstruction(nameOfCommand, getRSRegister(getRD()), 
                String.valueOf(immI()),
                getRSRegister(getRS1())
            );
    }

    private String parseSBorSHorSW() {
        String nameOfCommand = UNKNOWN;
        switch (getFunct3()) {
        case 0b000 -> {
            nameOfCommand = "sb";
        }
        case 0b001 -> {
            nameOfCommand = "sh";
        }
        case 0b010 -> {
            nameOfCommand = "sw";
        }
        }

        return LOADorSTOREorJALRInstruction(nameOfCommand, getRSRegister(getRS2()), 
            String.valueOf(immS()),
            getRSRegister(getRS1()) 
            );
    }

    private String parseBEQorBNEorBLTorBGEorBLTUorBGEU(Map<Integer, String> symtab) {
        String nameOfCommand = UNKNOWN;
        switch (getFunct3()) {
            case 0b000 -> {
                nameOfCommand = "beq";
            }
            case 0b001 -> {
                nameOfCommand = "bne";
            }
            case 0b100 -> {
                nameOfCommand = "blt";
            }
            case 0b101 -> {
                nameOfCommand = "bge";
            }
            case 0b110 -> {
                nameOfCommand = "bltu";
            }
            case 0b111 -> {
                nameOfCommand = "bgeu";
            }
        }

        String label = getLabel(immB(), symtab);


        return threeArgumentsInstruction(nameOfCommand, getRSRegister(getRS1()), getRSRegister(getRS2()), label);
    }

    private String parseJAL(Map<Integer, String> symtab) {
        int offset = immJ();

        String label = getLabel(offset, symtab);

        return twoArgumentsInstruction("jal", getRSRegister(getRD()), label);
    }

    private String getLabel(int offset, Map<Integer, String> symtab) {
        int findingAddress = address + offset;
        return String.format("%05x <%s>", findingAddress, symtab.get(findingAddress));
    }

    private String threeArgumentsInstruction(String name, String arg1, String arg2, String arg3) {
        if (name.equals(UNKNOWN)) {
            return unknownCommand();
        } else {
            return String.format("   %05x:\t%08x\t%7s\t%s, %s, %s\n", address, data, name, arg1, arg2, arg3);
        }
    }

    private String unknownCommand() {
        return String.format("   %05x:\t%08x\tunknown command\n", address, data);
    }

    private String twoArgumentsInstruction(String name, String arg1, String arg2) {
        if (name.equals(UNKNOWN)) {
            return unknownCommand();
        } else {
            return String.format("   %05x:\t%08x\t%7s\t%s, %s\n", address, data, name, arg1, arg2);
        }
    }

    private String fenceInstruction(String name) {
        if (name.equals(UNKNOWN)) {
            return unknownCommand();
        } else {
            return String.format("   %05x:\t%08x\t%7s\n", address, data, name);
        }
    }

    private String LOADorSTOREorJALRInstruction(String name, String arg1, String arg2, String arg3) {
        if (name.equals(UNKNOWN)) {
            return unknownCommand();
        } else {
            return String.format("   %05x:\t%08x\t%7s\t%s, %s(%s)\n", address, data, name, arg1, arg2, arg3);
        }
    }

    private String noArgumentsInstruction(String name) {
        if (name.equals(UNKNOWN)) {
            return unknownCommand();
        } else {
            return String.format("   %05x:\t%08x\t%7s\n", address, data, name);
        }
    }

    private int makeSigned(int number, int signedBit) {
        if ((number & (1 << signedBit)) != 0) {
            number = -((1 << signedBit + 1) - number);
        }
        return number;
    }

    private int getOpcode() {
        return data & 0b0111_1111;
    }

    private int getRD() {
        return (data >>> 7) & 0b11111;
    }

    private int getFunct3() {
        return (data >>> 12) & 0b111;
    }

    private int getFunct7() {
        return (data >>> 25) & 0b0111_1111;
    }

    private int getRS1() {
        return (data >>> 15) & 0b11111;
    }

    private int getRS2() {
        return (data >>> 20) & 0b11111;
    }

    private int immI() {
        return makeSigned(data >>> 20, 11);
    }

    private int immS() {
        return makeSigned(
        (((data >>> 25) & 0b1111111) << 5)
            |
        ((data >>> 7) & 0b11111), 11);
    }

    private int immB() {
        return makeSigned(
            (((data >>> 8) & 0b1111) << 1) |
            (((data >>> 25) & 0b111111) << 5) |
            (((data >>> 7) & 0b1) << 11) |
            (((data >>> 31) & 0b1) << 12), 
            12);
    }

    private int immU() {
        return makeSigned(
            data >>> 12, 31-12); 
    }

    private int immJ() {
        return makeSigned(
            (((data >>> 21) & (0b1111111111)) << 1) |
            (((data >>> 20) & (0b1)) << 11) |
            (((data >>> 12) & (0b11111111)) << 12) |
            (((data >>> 31) & (0b1)) << 20), 20);
    }

    private String getRSRegister(int number) {
        return switch (number) {
        case 0 -> "zero";
        case 1 -> "ra";
        case 2 -> "sp";
        case 3 -> "gp";
        case 4 -> "tp";
        case 5 -> "t0";
        case 6 -> "t1";
        case 7 -> "t2";
        case 8 -> "s0";
        case 9 -> "s1";
        case 10, 11, 12, 13, 14, 15, 16, 17 -> {
            yield "a" + number % 10;
        }
        case 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 -> {
            yield "s" + (number - 16);
        }
        case 28, 29, 30, 31 -> {
            yield "t" + (number - 25);
        }
        default -> "ERROR";
        };
    }
}