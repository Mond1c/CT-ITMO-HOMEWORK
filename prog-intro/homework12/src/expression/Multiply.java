package expression;

public class Multiply extends BinaryOperation {
    public Multiply(PartOfExpression left, PartOfExpression right) {
        super(left, right, "*", 1);
    }


    @Override
    protected int calculate(int x, int y) {
        return x * y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x * y;
    }
}
