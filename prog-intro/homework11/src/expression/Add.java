package expression;

public class Add extends BinaryOperation {
    public Add(PartOfExpression left, PartOfExpression right) {
        super(left, right, "+", 0);
    }

    @Override
    protected int calculate(int x, int y) {
        return x + y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x + y;
    }
}
