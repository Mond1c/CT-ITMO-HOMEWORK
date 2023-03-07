package expression.exceptions;

public class OverflowException extends ExpressionException {
    public OverflowException(String operationThatCreatesOverflow) {
        super(String.format("Overflow exception: %s", operationThatCreatesOverflow));
    }

    public OverflowException(String message, int pos) {
        super("Overflow exception: " + message, pos);
    }
}
