package expression;

public class Count extends UnaryOperation {
    public Count(PartOfExpression part) {
        super("count", 3, part);
    }

    @Override
    protected int calculate(int x) {
        return Integer.bitCount(x);
    }

    @Override
    protected double calculate(double x) {
        throw new AssertionError("Unsupported operation for double");
    }
}
