package expression;

public class Divide extends BinaryOperation {
    public Divide(PartOfExpression left, PartOfExpression right) {
        super(left, right, Operation.DIVIDE);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) / right.evaluate(value);
    }

    @Override
    public double evaluate(double x) {
        return left.evaluate(x) / right.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return left.evaluate(x, y, z) / right.evaluate(x, y, z);
    }
}
