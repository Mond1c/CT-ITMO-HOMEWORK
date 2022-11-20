import java.util.Scanner;

public class Human implements Player {
    private final Scanner scanner;

    public Human(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move makeMove(Position position) {
        System.out.println();
        System.out.println("Current position");
        System.out.println(position);
        System.out.println("Enter your move for " + position.getTurn());
        return new Move(scanner.nextInt() - 1, scanner.nextInt() - 1, position.getTurn());
    }
}
