package expression.exceptions;

public class NotValidArgument extends ExpressionException {
    public NotValidArgument(String caller, String args) {
        super(String.format("Bad arguments for %s: %s", caller, args));
    }
}
