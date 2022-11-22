import game.MnkBoard;
import game.Cell;
import game.Move;
import game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Winner implements Player {

    private final int m;
    private final int n;
    private final int k;

    private final int[][] score;
    private final MnkBoard board;
    private final Random random;

    public Winner(final int m, final int n, final int k, final MnkBoard board) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.score = new int[m][n];
        this.board = board;
        this.random = new Random();
    }

    private int getScoreInDirection(int startRow, int startColumn, int rowDir, int columnDir) {
        while (rowDir != 0 && 0 <= startRow - rowDir && startRow - rowDir < m) {
            startRow -= rowDir;
        }
        while (columnDir != 0 && 0 <= startColumn - columnDir && startColumn - columnDir < m) {
            startColumn -= columnDir;
        }
        int currentScore = 0;
        int currentOtherPlayerScore = 0;
        int row = startRow;
        int column = startColumn;
        while (0 <= row && row < m
                && 0 <= column && column < n
                && board.getCell(row, column) == board.getTurn()) {
            currentScore++;
            row += rowDir;
            column += columnDir;
        }
        if (currentScore == k - 1) {
            return Integer.MAX_VALUE;
        }
        row = startRow;
        column = startColumn;
        while (0 <= row && row < m
                && 0 <= column && column < n
                && board.getCell(row, column) != board.getTurn()
                && board.getCell(row, column) != Cell.E) {
            currentOtherPlayerScore++;
            row += rowDir;
            column += columnDir;
        }
        if (currentOtherPlayerScore == k - 1) {
            return Integer.MAX_VALUE - 1;
        }
        row = startRow;
        column = startColumn;
        currentScore = 0;
        currentOtherPlayerScore = 0;
        while (0 <= row && row < m
                && 0 <= column && column < n) {
            if (board.getCell(row, column) == board.getTurn()) {
                currentScore++;
            } else if (board.getCell(row, column) != Cell.E) {
                currentOtherPlayerScore++;
            }
            row += rowDir;
            column += columnDir;
        }
        return Math.max(currentScore, currentOtherPlayerScore);
    }

    private void updateScore() {
        for (int row = 0; row < m; row++) {
            for (int column = 0; column < n; column++) {
                if (board.getCell(row, column) == Cell.E) {
                    score[row][column]
                            = getScoreInDirection(row, column, 1, 0)
                            + getScoreInDirection(row, column, 1, 1)
                            + getScoreInDirection(row, column, 0, 1)
                            + getScoreInDirection(row, column, -1, 1);
                } else {
                    score[row][column] = Integer.MIN_VALUE;
                }
            }
        }
    }

    @Override
    public Move makeMove(final Cell turn) {
        updateScore();
        int maxScore = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maxScore = Math.max(maxScore, score[i][j]);
            }
        }
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (score[i][j] == maxScore) {
                    moves.add(new Move(i, j, turn));
                }
            }
        }
        return moves.get(random.nextInt(moves.size()));
    }
}
