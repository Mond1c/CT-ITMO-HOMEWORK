package game;

import java.io.PrintStream;
import java.util.List;

public class Tournament {
    private static final int WIN_POINTS = 3;
    private static final int DRAW_POINTS = 1;

    private final int m;
    private final int n;
    private final int k;
    private final List<Player> players;
    private final PrintStream output;
    private final List<MnkBoard.BlockedCell> blockedCells;
    private final int[] points;

    public Tournament(int m, int n, int k, final List<Player> players,
                      final List<MnkBoard.BlockedCell> blockedCells, final PrintStream output) {
        if (players.size() < 2) {
            throw new RuntimeException("You can't start tournament with only one (zero) player");
        }
        this.m = m;
        this.n = n;
        this.k = k;
        this.players = players;
        this.output = output;
        this.blockedCells = blockedCells;
        this.points = new int[players.size()];
    }

    private void displayResults() {
        output.print("Players | ");
        for (int i = 0; i < points.length; i++) {
            output.print((i + 1) + " ");
        }
        output.println();
        output.print("Points  | ");
        for (int point : points) {
            output.print(point + " ");
        }
    }

    private void playGame(int player1Number, int player2Number) {
        output.println("Player " + player1Number + " plays with player " + player2Number );
        final int result =
                new MnkGame(new MnkBoard(m, n, k, blockedCells), players.get(player1Number),
                        players.get(player2Number), output).play(false);
        switch (result) {
            case 1 -> {
                output.println("Player " + player1Number + " won");
                points[player1Number] += WIN_POINTS;
            }
            case 2 -> {
                output.println("Player " + player2Number + " won");
                points[player2Number] += WIN_POINTS;;
            }
            case 0 -> {
                output.println("Players " + player1Number + " and " + player2Number + " had draw.");
                points[player1Number] += DRAW_POINTS;
                points[player2Number] += DRAW_POINTS;
            }
            default -> {
                throw new AssertionError("Unknown result " + result);
            }
        }
    }

    public void play() {
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                playGame(i, j);
                playGame(j, i);
            }
        }
        displayResults();
    }
}
