package expression;

public abstract class PartOfExpression implements Expression, DoubleExpression, TripleExpression {

    protected final String operation;

    public PartOfExpression(String operation) {
        this.operation = operation;
    }


    protected abstract String buildMiniString(boolean isBracketsNeededOnTheLeftSide, boolean isBracketsNeededOnTheRightSide);
}
