package expression;

public abstract class BinaryOperation extends PartOfExpression {
    protected PartOfExpression left;
    protected PartOfExpression right;


    public BinaryOperation(final PartOfExpression left, final PartOfExpression right, final String operation) {
        super(operation);
        this.left = left;
        this.right = right;
    }

    public BinaryOperation(final String operation) {
        super(operation);
    }

    public void setLeft(final PartOfExpression left) {
        this.left = left;
    }

    public void setRight(final PartOfExpression right) {
        this.right = right;
    }

    @Override
    public int evaluate(final int x) {
        return this.evaluate(x, 0, 0);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operation + " " + right.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return buildMiniString(false, false);
    }


    @Override
    protected String buildMiniString(boolean isBracketsNeededOnTheLeftSide, boolean isBracketsNeededOnTheRightSide) {
        final StringBuilder builder = new StringBuilder();
        if (isBracketsNeededOnTheLeftSide) {
            builder.append('(');
        }
        builder.append(left.toMiniString());
        if (isBracketsNeededOnTheLeftSide) {
            builder.append(')');
        }
        builder.append(" ").append(operation).append(" ");
        if (isBracketsNeededOnTheRightSide) {
            builder.append('(');
        }
        builder.append(right.toMiniString());
        if (isBracketsNeededOnTheRightSide) {
            builder.append(')');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BinaryOperation otherOperation)) {
            return false;
        }
        return otherOperation.operation.equals(operation) && left.equals(otherOperation.left) && right.equals(otherOperation.right);
    }

    @Override
    public int hashCode() {
        return left.hashCode() + 111 * operation.hashCode() + 111111 * right.hashCode();
    }
}
