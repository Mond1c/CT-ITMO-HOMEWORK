package expression;

public abstract class PartOfExpression implements Expression, DoubleExpression, TripleExpression {

    protected final String operation;

    public PartOfExpression(String operation) {
        this.operation = operation;
    }

    @Override
    public int evaluate(final int x) {
        return evaluate(x, 0, 0);
    }

    public String getOperation() {
        return operation;
    }


    protected abstract String buildMiniString(boolean isBracketsNeededOnTheLeftSide, boolean isBracketsNeededOnTheRightSide);
}
