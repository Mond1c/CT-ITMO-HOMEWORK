package expression;

public class Multiply extends BinaryOperation {
    public Multiply(PartOfExpression left, PartOfExpression right) {
        super(left, right, "*");
    }

    @Override
    public int evaluate(final int value) {
        return left.evaluate(value) * right.evaluate(value);
    }

    @Override
    public double evaluate(final double x) {
        return left.evaluate(x) * right.evaluate(x);
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return left.evaluate(x, y, z) * right.evaluate(x, y, z);
    }

    @Override
    public String toMiniString() {
        return buildMiniString(left.getClass() == Add.class || left.getClass() == Subtract.class,
                right.getClass() == Add.class || right.getClass() == Subtract.class || right.getClass() == Divide.class);
    }
}
