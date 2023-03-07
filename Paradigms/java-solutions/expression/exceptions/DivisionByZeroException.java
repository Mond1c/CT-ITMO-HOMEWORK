package expression.exceptions;

public class DivisionByZeroException extends ExpressionException {
    public DivisionByZeroException(String leftArg, String rightArg) {
        super(String.format("Division by zero: %s / %s", leftArg, rightArg));
    }
}
