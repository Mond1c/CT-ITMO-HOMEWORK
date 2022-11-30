package expression;

public class Subtract extends  BinaryOperation {
    public Subtract(final PartOfExpression left, final PartOfExpression right) {
        super(left, right, Operation.SUBTRACT);
    }

    @Override
    public int evaluate(final int value) {
        return left.evaluate(value) - right.evaluate(value);
    }

    @Override
    public double evaluate(final double x) {
        return left.evaluate(x) - right.evaluate(x);
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        // :NOTE: copypaste
        return left.evaluate(x, y, z) - right.evaluate(x, y, z);
    }
}
