package expression.exceptions;

public class UnsupportedCharacter extends RuntimeException {
    public UnsupportedCharacter() {}

    public UnsupportedCharacter(final String message) {
        super(message);
    }
}
