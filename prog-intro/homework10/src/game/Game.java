package game;

public class Game {
    private final MnkBoard board;
    private final Player player1;
    private final Player player2;

    public Game(MnkBoard board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(boolean log) {
        while (true) {
            final int result1 = makeMove(player1, 1, log);
            if (result1 != -1) {
                System.out.println("Final position");
                System.out.println(board);
                return result1;
            }
            final int result2 = makeMove(player2, 2, log);
            if (result2 != -1) {
                System.out.println("Final position");
                System.out.println(board);
                return result2;
            }
        }
    }

    private Move getMoveFromPlayer(Player player) {
        System.out.println();
        System.out.println("Current position");
        System.out.println(board);
        System.out.println("Enter your move for " + board.getTurn());
        return player.makeMove(board.getTurn());
    }

    private int makeMove(Player player, int no, boolean log) {
        Move move;
        try {
            move = getMoveFromPlayer(player);
            while (!board.isValid(move)) {
                System.out.println("You entered incorrect move. Try again.");
                move = getMoveFromPlayer(player);
            }
        } catch (RuntimeException e) {
            return 3 - no;
        }
        final GameResult result = board.makeMove(move);
        if (log) {
            System.out.println();
            System.out.println("game.Player: " + no);
            System.out.println(move);
            System.out.println(board);
            System.out.println("Result: " + result);
        }
        switch (result) {
            case WIN -> {
                return no;
            }
            case LOOSE -> {
                return 3 - no;
            }
            case DRAW -> {
                return 0;
            }
            case UNKNOWN -> {
                return -1;
            }
            default -> {
                throw new AssertionError("Unknown makeMove result " + result);
            }
        }
    }
}
