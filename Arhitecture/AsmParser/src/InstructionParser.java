public class InstructionParser {
    public static String parseRiscVInstruction(final String str, final int addr) {
        if (str.equals("00000000000000000000000001110011")) {
            return "ebreak";
        } else if (str.equals("00000000000100000000000001110011")) {
            return "ecall";
        }
        final String funct7 = str.substring(0, 7);
        final String rs2 = str.substring(7, 12);
        final String rs1 = str.substring(12, 17);
        final String funct3 = str.substring(17, 20);
        final String rd = str.substring(20, 25);
        final String opcode = str.substring(25, 32);
        final int code = Integer.parseInt(str, 2);
        switch (opcode) {
            case "0110011": {
                return toThreeArgumentsInstructionString(addr, code, RiscVParser.parseR(funct7, funct3),
                        Register.parse(rd), Register.parse(rs1), Register.parse(rs2));
            }
            case "1110011": {
                return toThreeArgumentsInstructionString(addr, code, RiscVParser.parseIC(funct3),
                        Register.parse(rd), Register.parse(str.substring(0, 12)), Register.parse(rs1));
            }
            case "0000011": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(0)).repeat(20) +
                        str.substring(0, 12), 2);
                return toLSJInstructionString(addr, code, RiscVParser.parseIL(funct3),
                        Register.parse(rd), String.valueOf(imm), Register.parse(rs1));
            }
            case "0100011": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(0)).repeat(20) +
                        str.substring(0, 7), 2);
                return toLSJInstructionString(addr, code, RiscVParser.parseS(funct3),
                        Register.parse(rs2), String.valueOf(imm), Register.parse(rs1));
            }
            case "1100011": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(0)).repeat(20) +
                        "0".repeat(12), 2);
                return toThreeArgumentsInstructionString(addr, code, RiscVParser.parseB(funct3),
                        Register.parse(rs1), Register.parse(rs2), "Label");
            }
            case "0110111", "0010111": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(0)).repeat(20) +
                        "0".repeat(12), 2);
                return toTwoArgumentsInstructionString(addr, code, RiscVParser.parseU(opcode),
                        Register.parse(rd), String.valueOf(imm));
            }
            case "0010011": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(0)).repeat(20) +
                        str.substring(0, 12), 2);
                final String reg;
                final int fCode = Integer.parseInt(funct3, 2);
                if (fCode == 1 || fCode == 3) {
                    reg = String.valueOf(Integer.parseUnsignedInt(str.substring(7, 12), 2));
                } else {
                    reg = String.valueOf(imm);
                }
                return toThreeArgumentsInstructionString(addr, code, RiscVParser.parseIR(funct7, funct3),
                        Register.parse(rd), Register.parse(rs1), reg);
            }
            case "1101111": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(0)).repeat(12) +
                        str.substring(12, 20) + str.charAt(11) + str.substring(1, 11) + "0", 2);
                return toThreeArgumentsInstructionString(addr, code, RiscVParser.parseJ(opcode),
                        Register.parse(rd), Register.parse(rs1), String.valueOf(imm));
            }
            case "1100111": {
                int imm = Integer.parseUnsignedInt(String.valueOf(str.charAt(20)).repeat(20) +
                        str.substring(0, 12), 2);
                return toThreeArgumentsInstructionString(addr, code, RiscVParser.parseJ(opcode),
                        Register.parse(rd), Register.parse(rs1), String.valueOf(imm));
            }
            case "0001111": {
                return RiscVParser.parseFence(funct3);
            }
        }
        throw new UnsupportedInstruction(String.format("Unsupported instruction with code = %s", str));
    }

    private static String toTwoArgumentsInstructionString(final int addr, final int code, final String instruction,
                                                          final String arg1, final String arg2) {
        return String.format("   %05x:\t%08x\t%7s\t%s, %s\n", addr, code, instruction,
                arg1, arg2);
    }

    private static String toThreeArgumentsInstructionString(final int addr, final int code, final String instruction,
                                                     final String arg1, final String arg2, final String arg3) {
        return String.format("   %05x:\t%08x\t%7s\t%s, %s, %s\n", addr, code, instruction,
                arg1, arg2, arg3);
    }

    private static String toLSJInstructionString(final int addr, final int code, final String instruction,
                                                 final String arg1, final String arg2, final String arg3) {
        return String.format("   %05x:\t%08x\t%7s\t%s, %s(%s)\n", addr, code, instruction,
                arg1, arg2, arg3);
    }
}
