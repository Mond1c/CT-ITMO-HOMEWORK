package expression;

public class Subtract extends  BinaryOperation {
    public Subtract(final PartOfExpression left, final PartOfExpression right) {
        super(left, right, "-", 0);
    }

    @Override
    protected int calculate(int x, int y) {
        return x - y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x - y;
    }
}
