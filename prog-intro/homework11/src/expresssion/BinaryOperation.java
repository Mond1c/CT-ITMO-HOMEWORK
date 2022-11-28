package expresssion;

public abstract class BinaryOperation implements Expression {
    protected final Expression left;
    protected final Expression right;
    private final Operation operation;

    public BinaryOperation(final Expression left, final Expression right, final Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operation.toString() + " " + right.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return getMiniString(this.operation);
    }

    @Override
    public boolean equals(final Expression other) {
        return toString().equals(other.toString());
    }

    public String getMiniString(Operation operation) {
        final StringBuilder builder = new StringBuilder();
        boolean isBracketsNeeded = (this.operation == Operation.ADD || this.operation == Operation.SUBTRACT)
                && (operation == Operation.MULTIPLY || operation == Operation.DIVIDE);
        if (isBracketsNeeded) {
            builder.append('(');
        }
        builder.append(left.getMiniString(this.operation)).append(" ").append(operation)
                .append(" ").append(right.getMiniString(this.operation));
        if (isBracketsNeeded) {
            builder.append(')');
        }
        return builder.toString();
    }
}
