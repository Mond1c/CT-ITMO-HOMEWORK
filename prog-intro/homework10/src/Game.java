import java.util.Scanner;

public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private final Scanner scanner;

    public Game(final Scanner scanner) {
        this.scanner = scanner;
        this.board = new Board(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        this.player1 = new Player(board, Board.Cell.BLUE);
        this.player2 = new Player(board, Board.Cell.RED);
    }

    public void play() {
        Board.Cell winner = board.getWinner();
        boolean isPlayer1Move = true;
        while (winner == Board.Cell.EMPTY) {
            if (isPlayer1Move) {
                player1.move(scanner);
            } else {
                player2.move(scanner);
            }
            System.out.println(board);
            isPlayer1Move = !isPlayer1Move;
            winner = board.getWinner();
        }
    }
}
