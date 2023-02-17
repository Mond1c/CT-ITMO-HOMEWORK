package expression;

public class Negate extends UnaryOperation {
    public Negate(final PartOfExpression partOfExpression) {
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
