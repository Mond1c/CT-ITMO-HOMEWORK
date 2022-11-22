package game;

public interface Position {
    Cell getTurn();
    boolean isValid(final Move move);
    Cell getCell(int row, int column);
}
