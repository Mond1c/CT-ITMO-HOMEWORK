import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Write board row's count");
        final int m = scanner.nextInt();
        System.out.println("Write board column's count");
        final int n = scanner.nextInt();
        System.out.println("Write length of winner combination");
        final int k = scanner.nextInt();
        final Board board = new Board(m, n, k);
        final int result = new Game(board,
                new Human(scanner),
                new Winner(m, n, k, board)).play(false);
        switch (result) {
            case 1 -> {
                System.out.println("First player won!");
                break;
            }
            case 2 -> {
                System.out.println("Second player won!");
                break;
            }
            case 0 -> {
                System.out.println("Draw");
                break;
            }
            default -> {
                throw new AssertionError("Unknown result " + result);
            }
        }
    }
}
