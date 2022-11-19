import java.util.Scanner;

public class Player {
    private final Board board;
    private final Board.Cell cell;

    public Player(Board board, Board.Cell cell) {
        this.board = board;
        this.cell = cell;
    }

    public void move(final Scanner scanner) {
        int x = scanner.nextInt(), y = scanner.nextInt();
        while (x < 1 || x > board.rowSize() ||
                y < 1 || y > board.columnSize() ||
                !board.moveTo(x - 1, y - 1, cell)) {
            x = scanner.nextInt();
            y = scanner.nextInt();
        }
    }
}
