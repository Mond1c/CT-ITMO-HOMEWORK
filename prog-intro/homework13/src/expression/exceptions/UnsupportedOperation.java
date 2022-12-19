package expression.exceptions;

public class UnsupportedOperation extends RuntimeException {
    public UnsupportedOperation() {

    }

    public UnsupportedOperation(final String message) {
        super(message);
    }
}
