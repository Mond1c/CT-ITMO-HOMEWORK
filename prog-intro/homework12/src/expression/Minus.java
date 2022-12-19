package expression;

public class Minus extends UnaryOperation {
    public Minus(final PartOfExpression partOfExpression) {
        super("-", 3, partOfExpression);
    }

    @Override
    protected int calculate(int x) {
        return -x;
    }

    @Override
    protected double calculate(double x) {
        return -x;
    }
}
