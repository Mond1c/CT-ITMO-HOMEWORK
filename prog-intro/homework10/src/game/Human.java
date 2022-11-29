package game;

import java.util.Scanner;

public class Human implements Player {
    private final Scanner scanner;

    public Human(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move makeMove(Cell turn) {
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
