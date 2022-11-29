package game;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

public class Game {
    private final MnkBoard board;
    private final PrintStream output;
    private final Player player1;
    private final Player player2;

    public Game(final MnkBoard board, final Player player1, final Player player2, final PrintStream output) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.output = output;
    }

    public int play(boolean log) {
        while (true) {
            final int result1 = makeMove(player1, 1, log);
            if (result1 != -1) {
                output.println("Final position");
                output.println(board);
                return result1;
            }
            final int result2 = makeMove(player2, 2, log);
            if (result2 != -1) {
                output.println("Final position");
                output.println(board);
                return result2;
            }
        }
    }

    private Move getMoveFromPlayer(Player player) { // :NOTE: а что если игрок по сети (DONE)
        output.println();
        output.println("Current position");
        output.println(board);
        output.println("Enter your move for " + board.getTurn());
        return player.makeMove(board.getTurn());
    }

    private int makeMove(Player player, int no, boolean log) {
        Move move;
        try {
            move = getMoveFromPlayer(player);
            while (!board.isValid(move)) {
                output.println("You entered incorrect move. Try again.");
                move = getMoveFromPlayer(player);
            }
        } catch (NoSuchElementException e) { // Если игрок звкрыл сканнер Ctrl(Command)+D, то он проигрывает игру и турнир
            return 3 - no;
        }
        final GameResult result = board.makeMove(move);
        if (log) {
            output.println();
            output.println("game.Player: " + no);
            output.println(move);
            output.println(board);
            output.println("Result: " + result);
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
