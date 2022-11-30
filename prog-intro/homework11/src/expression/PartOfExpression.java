package expression;

public abstract class PartOfExpression implements Expression, DoubleExpression, TripleExpression {
   // public abstract boolean equals(final PartOfExpression other);

    protected final Operation operation;

    public PartOfExpression(Operation operation) {
        this.operation = operation;
    }

    protected abstract String getMiniString(boolean isBracketsNeeded);
}
