package expression;

public class Set extends BinaryOperation {
    public Set(PartOfExpression left, PartOfExpression right) {
        super(left, right, "set", 0);
    }

    @Override
    protected int calculate(int x, int y) {
        int clearBit = ~(1 << y);
        int mask = x & clearBit;
        return mask | (1 << y);
    }

    @Override
    protected double calculate(double x, double y) {
        throw new AssertionError("Unsupported operation for double data type");
    }
}
