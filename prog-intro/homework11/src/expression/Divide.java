package expression;

public class Divide extends BinaryOperation {
    public Divide(PartOfExpression left, PartOfExpression right) {
        super(left, right, "/", 1);
    }


    @Override
    protected int calculate(int x, int y) {
        return x / y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x / y;
    }
}
