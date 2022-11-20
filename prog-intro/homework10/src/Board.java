import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Position {
    private final Cell[][] field;
    private final int m;
    private final int n;
    private final int k;
    private int emptyCellsCount;
    private Cell turn;

    private final List<Move> winMovesO;
    private final List<Move> winMovesX;

    public Board(int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.emptyCellsCount = m * n;
        this.turn = Cell.X;
        this.field = new Cell[m][n];
        this.winMovesO = new ArrayList<>();
        this.winMovesX = new ArrayList<>();
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && field[move.getRow()][move.getColumn()] == Cell.E
                && turn == move.getValue();
    }

    @Override
    public Cell getCell(int row, int column) {
        return field[row][column];
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(" ");
        for (int column = 1; column <= n; column++) {
            builder.append(column);
        }
        builder.append(System.lineSeparator());
        for (int row = 0; row < m; row++) {
            builder.append(row + 1);
            for (Cell cell : field[row]) {
                builder.append(cell.toString());
            }
            builder.append(System.lineSeparator());
        }
        builder.setLength(builder.length() - System.lineSeparator().length());
        return builder.toString();
    }

    public GameResult makeMove(Move move) { // Time complexity = O(k)
        field[move.getRow()][move.getColumn()] = move.getValue();
        emptyCellsCount--;
        if (checkWin(move)) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }
        turn = turn == Cell.X ? Cell.O : Cell.X;
        // This part of code calculate, who can win on the next move.
        // TODO: Extract this code into methods to make it more readable.
        if (turn == Cell.X && !winMovesX.isEmpty()) {
            if (winMovesX.size() > 1) {
                return GameResult.LOOSE;
            } else {
                final Move nextMove = winMovesX.get(0);
                if (field[nextMove.getRow()][nextMove.getColumn()] == Cell.E) {
                    field[nextMove.getRow()][nextMove.getColumn()] = turn;
                    return GameResult.LOOSE;
                } else {
                    winMovesX.remove(0);
                }
            }
        } else if (turn == Cell.O && !winMovesO.isEmpty()){
            if (winMovesO.size() > 1) {
                return GameResult.LOOSE;
            } else {
                final Move nextMove = winMovesO.get(0);
                if (field[nextMove.getRow()][nextMove.getColumn()] == Cell.E) {
                    field[nextMove.getRow()][nextMove.getColumn()] = turn;
                    return GameResult.LOOSE;
                } else {
                    winMovesO.remove(0);
                }
            }
        }
        if (emptyCellsCount == 1) {
            return GameResult.DRAW;
        }
        return GameResult.UNKNOWN;
    }

    private int checkDirection(int row, int column, int rowDir, int columnDir, int startCount) { // Time complexity = O(k)
        int count = startCount;
        while (0 <= row && row < m
                && 0 <= column && column < n
                && field[row][column] == turn) {
            row += rowDir;
            column += columnDir;
            count++;
        }
        if (count == k - 1) {
            final Move move = new Move(row, column, turn);
            if (!isValid(move)) {
                return count;
            }
            if (turn == Cell.O) {
                winMovesO.add(move);
            } else {
                winMovesX.add(move);
            }
        }
        return count;
    }

    private boolean checkAllDirections(int row, int column) { // Time complexity = 8k = O(k)
        return checkDirection(row, column, 1, 0, checkDirection(row, column, -1, 0, 0) - 1) >= k
                || checkDirection(row, column, 1, 1, checkDirection(row, column, -1, -1, 0) - 1) >= k
                || checkDirection(row, column, 0, 1, checkDirection(row, column, 0, -1, 0) - 1) >= k
                || checkDirection(row, column, -1, 1, checkDirection(row, column, 1, -1, 0) - 1) >= k;
    }

    private boolean checkWin(Move move) { // Time complexity = O(K)
        return checkAllDirections(move.getRow(), move.getColumn());
    }

    private boolean checkDraw() {
        return emptyCellsCount == 0;
    }
}
