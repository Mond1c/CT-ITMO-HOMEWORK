package expression.exceptions;

public class ExpressionException extends ArithmeticException {
    public ExpressionException(String message, int pos) {
        super(String.format("%s at position %d", message, pos));
    }

    public ExpressionException(String message) { super(message); }
}
