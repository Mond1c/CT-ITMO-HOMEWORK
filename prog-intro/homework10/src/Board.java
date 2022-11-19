public class Board {
    public static enum Cell {
        RED {
            @Override
            public String toString() {
                return "X";
            }
        },
        BLUE {
            @Override
            public String toString() {
                return "0";
            }
        },
        EMPTY {
            @Override
            public String toString() {
                return " ";
            }
        },
    }

    private final Cell[][] cells;
    private final int k;

    public Board(int m, int n, int k) {
        this.cells = new Cell[m][n];
        this.k = k;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                this.cells[i][j] = Cell.EMPTY;
            }
        }
    }

    public boolean moveTo(int x, int y, Cell cell) {
        if (cells[x][y] != Cell.EMPTY) {
            return false;
        }
        cells[x][y] = cell;
        return true;
    }

    private boolean isWinner(Cell cell, int x, int y, int dirX, int dirY) {
        int count = 0;
        while (x + dirX < cells.length && x + dirX >= 0
                && y + dirY < cells[x].length && y + dirY >= 0 && count < k && cells[x + dirX][y + dirY] == cell) {
            dirX += dirX;
            dirY += dirY;
            count++;
        }
        return count == k;
    }

    private boolean isWinner(Cell cell, int x, int y) {
        return isWinner(cell, x, y, 1, 0) || isWinner(cell, x, y, 1, 1)
                || isWinner(cell, x, y, 0, 1) || isWinner(cell, x, y, -1, 1)
                || isWinner(cell, x, y, -1, 0) || isWinner(cell, x, y, 0, -1)
                || isWinner(cell, x, y, 1, -1);
    }

    public Cell getWinner() {
        for (int i = 0; i < cells.length; ++i) {
            for (int j = 0; j < cells[i].length; ++j) {
                if (cells[i][j] == Cell.EMPTY) {
                    continue;
                }
                if (isWinner(Cell.BLUE, i, j)) {
                    return Cell.BLUE;
                } else if (isWinner(Cell.RED, i, j)) {
                    return Cell.RED;
                }
            }
        }
        return Cell.EMPTY;
    }

    public int rowSize() {
        return cells.length;
    }

    public int columnSize() {
        return cells[0].length;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                builder.append(cell).append(" ");
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
