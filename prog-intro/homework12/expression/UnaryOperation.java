package expression;

public abstract class UnaryOperation implements PartOfExpression {
    private final String operation;

    private final int priority;
    private final PartOfExpression part;

    protected UnaryOperation(String operation, int priority, PartOfExpression part) {
        this.operation = operation;
        this.priority = priority;
        this.part = part;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toMiniString() {
        if (!(part instanceof BinaryOperation binaryOperation) || binaryOperation.getPriority() > getPriority()) {
            return operation + " " + part.toMiniString();
        }
        return operation + "(" + part.toMiniString() + ")";
    }

    @Override
    public String toString() {
        return operation + "(" + part.toString() + ")";
    }

    protected abstract int calculate(int x);
    protected abstract double calculate(double x);


    @Override
    public int evaluate(int x) {
        return calculate(part.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(part.evaluate(x, y, z));
    }

    @Override
    public double evaluate(double x) {
        return calculate(part.evaluate(x));
    }
}
