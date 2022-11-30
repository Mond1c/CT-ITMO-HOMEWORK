package expression;

public abstract class BinaryOperation extends PartOfExpression {
    protected final PartOfExpression left;
    protected final PartOfExpression right;


    public BinaryOperation(final PartOfExpression left, final PartOfExpression right, final Operation operation) {
        super(operation);
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operation.toString() + " " + right.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return getMiniString(false);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BinaryOperation otherOperation)) {
            return false;
        }
        return otherOperation.operation == operation && left.equals(otherOperation.left) && right.equals(otherOperation.right);
    }

    @Override
    public int hashCode() {
        return left.hashCode() + 2 * operation.hashCode() + 3 * right.hashCode();
    }

    @Override
    public String getMiniString(boolean isBracketsNeeded) {
        final StringBuilder builder = new StringBuilder();
        if (isBracketsNeeded) {
            builder.append('(');
        }
        boolean isBracketNeedForLeftSide = (operation == Operation.MULTIPLY || operation == Operation.DIVIDE)
                && (left.operation == Operation.ADD || left.operation == Operation.SUBTRACT);
        boolean isBracketNeedForRightSide = (operation == Operation.MULTIPLY || operation == Operation.DIVIDE)
                && (right.operation == Operation.ADD || right.operation == Operation.SUBTRACT)
                || (operation == Operation.SUBTRACT && (right.operation == Operation.SUBTRACT || right.operation == Operation.ADD))
                || ((operation == Operation.DIVIDE || operation == Operation.MULTIPLY) && right.operation == Operation.DIVIDE)
                || (operation == Operation.DIVIDE && right.operation == Operation.MULTIPLY);
        builder.append(left.getMiniString(isBracketNeedForLeftSide)).append(" ").append(operation)
                .append(" ").append(right.getMiniString(isBracketNeedForRightSide));
        if (isBracketsNeeded) {
            builder.append(')');
        }
        return builder.toString();
    }
}
