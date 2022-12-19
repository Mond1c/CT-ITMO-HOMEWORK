package expression;

public class Clear extends BinaryOperation {

    public Clear(PartOfExpression left, PartOfExpression right) {
        super(left, right, "clear", 0);
    }

    @Override
    protected int calculate(int x, int y) {
        int clearBit = ~(1 << y);
        int mask = x & clearBit;
        return mask;
    }

    @Override
    protected double calculate(double x, double y) {
        throw new AssertionError("Unsupported operation for double data type");
    }
}
