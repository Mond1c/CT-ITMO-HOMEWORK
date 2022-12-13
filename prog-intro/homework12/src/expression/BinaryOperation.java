package expression;

public abstract class BinaryOperation extends PartOfExpression {
    protected PartOfExpression left;
    protected PartOfExpression right;
    protected final int priority;


    public BinaryOperation(final PartOfExpression left, final PartOfExpression right, final String operation, final int priority) {
        super(operation);
        this.left = left;
        this.right = right;
        this.priority = priority;
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
        if (left instanceof BinaryOperation binaryOperation && binaryOperation.priority > priority) {
            builder.append('(');
        }
        builder.append(left.toMiniString());
        if (left instanceof BinaryOperation binaryOperation && binaryOperation.priority > priority) {
            builder.append(')');
        }
        builder.append(" ").append(operation).append(" ");
        if (right instanceof BinaryOperation binaryOperation && binaryOperation.priority > priority) {
            builder.append('(');
        }
        builder.append(right.toMiniString());
        if (right instanceof BinaryOperation binaryOperation && binaryOperation.priority > priority) {
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
