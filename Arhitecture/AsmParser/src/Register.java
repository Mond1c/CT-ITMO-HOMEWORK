public class Register {
    public static String parse(final String reg) {
        final int code = Integer.parseInt(reg, 2);
        return switch (code) {
            case 0 -> "zero";
            case 1 -> "ra";
            case 2 -> "sp";
            case 3 -> "gp";
            case 4 -> "tp";
            case 5, 6, 7 -> "t" + String.valueOf(code - 5);
            case 8 -> "s0";
            case 9 -> "s1";
            case 10, 11, 12, 13, 14, 15, 16, 17 -> "a" + String.valueOf(code - 10);
            case 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 -> "s" + String.valueOf(code - 16);
            case 28, 29, 30, 31 -> "t" + String.valueOf(code - 25);
            default -> throw new IllegalArgumentException("You can't use more than 32 registers");
        };
    }
}
