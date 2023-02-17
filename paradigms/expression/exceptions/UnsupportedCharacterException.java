package expression.exceptions;

public class UnsupportedCharacterException extends RuntimeException {
    public UnsupportedCharacterException() {}

    public UnsupportedCharacterException(final String message) {
        super(message);
    }
}
