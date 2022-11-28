package game;

public enum Cell {
    E("."),
    X("X"),
    O("0"),
    B("#");

    // :NOTE: constructor in enum with string token (DONE)
    private final String value;

    private Cell(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
