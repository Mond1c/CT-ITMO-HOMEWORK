package expression;

public abstract class BinaryOperation implements PartOfExpression {
    protected final PartOfExpression left;
    protected final PartOfExpression right;
    private final int priority;
    private final String operation;


    public BinaryOperation(final PartOfExpression left, final PartOfExpression right, final String operation, final int priority) {
        this.operation = operation;
        this.priority = priority;
        this.left = left;
        this.right = right;
    }

    protected abstract int calculate(int x, int y);
    protected abstract double calculate(double x, double y);

    @Override
    public int evaluate(int x) {
        return calculate(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return calculate(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operation + " " + right.toString() + ")";
    }

    private boolean isBracketNeededForLeftSide() {
        if (!(left instanceof BinaryOperation binaryOperation)) {
            return false;
        }
        return binaryOperation.priority < priority;
    }

    private boolean isBracketNeededForRightSide() {
        if (!(right instanceof BinaryOperation binaryOperation)) {
            return false;
        }
        return binaryOperation.priority < priority
                || (this instanceof Divide)
                || (this instanceof Subtract || right instanceof Divide)
                && priority == binaryOperation.priority;
    }

    @Override
    public String toMiniString() {
        final StringBuilder builder = new StringBuilder();
        boolean leftSide = isBracketNeededForLeftSide();
        boolean rightSide = isBracketNeededForRightSide();
        if (leftSide) {
            builder.append('(');
        }
        builder.append(left.toMiniString());
        if (leftSide) {
            builder.append(')');
        }
        builder.append(' ').append(operation).append(' ');
        if (rightSide) {
            builder.append('(');
        }
        builder.append(right.toMiniString());
        if (rightSide) {
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
