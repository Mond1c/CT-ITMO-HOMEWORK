package expression.exceptions;

public class UnsupportedOperationException extends RuntimeException {
    public UnsupportedOperationException() {

    }

    public UnsupportedOperationException(final String message) {
        super(message);
    }
}
