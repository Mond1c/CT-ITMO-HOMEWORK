package expression.generic;

import expression.exceptions.ParserException;

public class Main {
    // args[0] - Mode, args[1] - Expression
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad count of arguments.");
            System.out.println("Usage: Mode Expression");
            return;
        }

        try {
            final int x1 = -2, x2 = 2, y1 = -2, y2 = 2, z1 = -2, z2 = 2;
            Object[][][] table = new GenericTabulator().tabulate(args[0].substring(1), args[1], x1, x2, y1, y2, z1, z2);
            for (int i = 0; i <= x2 - x1; i++) {
                for (int j = 0; j <= y2 - y1; j++) {
                    for (int k = 0; k <= z2 - z1; k++) {
                        System.out.println("[" + (x1 + i) + ", " + (y1 + j) + ", " + (z1 + k) + "]:\t" + table[i][j][k]);
                    }
                }
            }

        } catch (ParserException E) {
            System.out.println("Error was happened: " + E.getMessage());
        }

    }
}
