public class RiscVParser {
    public static String parseR(final String funct7, final String funct3) {
        final int code = Integer.parseInt(funct3, 2);
        return switch (funct7) {
            case "0000000" -> switch (code) {
                case 0 -> "add";
                case 1 -> "sll";
                case 2 -> "slt";
                case 3 -> "sltu";
                case 4 -> "xor";
                case 5 -> "srl";
                case 6 -> "or";
                case 7 -> "and";
                default -> throw new UnsupportedInstruction(
                        String.format("Unsupported instruction with funct7 = %s and funct3 %s", funct7, funct3));
            };
            case "0100000" -> switch (code) {
                case 0 -> "sub";
                case 1 -> "sra";
                default -> throw new UnsupportedInstruction(
                        String.format("Unsupported instruction with funct7 = %s and funct3 %s", funct7, funct3));
            };
            case "0000001" -> switch (code) {
                case 0 -> "mul";
                case 1 -> "mulh";
                case 2 -> "mulhsu";
                case 3 -> "mulhu";
                case 4 -> "div";
                case 5 -> "divu";
                case 6 -> "rem";
                case 7 -> "remu";
                default -> throw new UnsupportedInstruction(
                        String.format("Unsupported instruction with funct7 = %s and funct3 %s", funct7, funct3));

            };
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with funct7 = %s and funct3 %s", funct7, funct3));
        };
    }

    public static String parseIR(final String funct7, final String funct3) throws UnsupportedInstruction {
        return switch (Integer.parseInt(funct3, 2)) {
            case 0 -> "addi";
            case 1 -> {
                if (funct7.equals("0000000")) {
                    yield "slli";
                }
                throw new UnsupportedInstruction("Invalid funct7 = %s for slli command");
            }
            case 2 -> "slti";
            case 3 -> "sltiu";
            case 4 -> "xori";
            case 5 -> {
                if (funct7.equals("0000000")) {
                    yield  "srli";
                } else if (funct7.equals("0100000")) {
                    yield "srai";
                }
                throw new UnsupportedInstruction(
                        String.format("Unsupported instruction with funct7 = %s and funct3 = %s", funct7, funct3));
            }
            case 6 -> "ori";
            case 7 -> "andi";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with funct7 = %s and funct3 = %s", funct7, funct3));
        };
    }

    public static String parseIL(final String funct3) {
        return switch (Integer.parseInt(funct3, 2)) {
            case 0 -> "lb";
            case 1 -> "lh";
            case 2 -> "lw";
            case 4 -> "lbu";
            case 5 -> "lhu";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with funct3 = %s", funct3));
        };
    }

    public static String parseIC(final String funct3) {
        return switch (Integer.parseInt(funct3, 2)) {
            case 1 -> "csrrw";
            case 2 -> "csrrs";
            case 3 -> "csrrc";
            case 5 -> "csrwi";
            case 6 -> "csrsi";
            case 7 -> "csrci";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with funct3 = %s", funct3));
        };
    }

    public static String parseS(final String funct3) {
        return switch (Integer.parseInt(funct3, 2)) {
            case 0 -> "sb";
            case 1 -> "sh";
            case 2 -> "sw";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with funct3 = %s", funct3));
        };
    }

    public static String parseB(final String funct3) {
        return switch (Integer.parseInt(funct3, 2)) {
            case 0 -> "beq";
            case 1 -> "bne";
            case 4 -> "blt";
            case 5 -> "bge";
            case 6 -> "bltu";
            case 7 -> "bgeu";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with funct3 = %s", funct3));
        };
    }

    public static String parseU(final String opcode) {
        return switch (opcode) {
            case "0110111" -> "lui";
            case "0010111" -> "auipc";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with opcode = %s", opcode));
        };
    }

    public static String parseJ(final String opcode) {
        return switch (opcode) {
            case "1101111" -> "jal";
            case "1100111" -> "jalr";
            default -> throw new UnsupportedInstruction(
                    String.format("Unsupported instruction with opcode = %s", opcode));
        };
    }

    public static String parseFence(final String funct3) {
        return switch (Integer.parseInt(funct3, 2)) {
            case 1 -> "fence.i";
            default -> "fence";
        };
    }
}
