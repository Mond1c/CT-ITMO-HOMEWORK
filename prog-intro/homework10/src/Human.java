import java.util.Scanner;

public class Human implements Player {
    private final Scanner scanner;

    public Human(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move makeMove(Cell turn) {
        return new Move(scanner.nextInt() - 1, scanner.nextInt() - 1, turn);
    }
}
