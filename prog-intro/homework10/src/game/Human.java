package game;

import java.io.PrintStream;
import java.util.Scanner;

public class Human implements Player {
    private final Scanner scanner;
    private final PrintStream output;

    public Human(final Scanner scanner, final PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    @Override
    public Move makeMove(Cell turn) {
        output.println("Enter your move for " + turn);
        if (!scanner.hasNextInt()) {
            scanner.next(); scanner.next();
            return new Move(-1, -1, turn);
        }
        final int row = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            scanner.next();
            return new Move(-1, -1, turn);
        }
        final int column = scanner.nextInt();
        return new Move(row - 1, column - 1, turn);
    }
}
