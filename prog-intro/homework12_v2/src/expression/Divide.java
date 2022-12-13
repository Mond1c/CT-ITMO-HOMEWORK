package expression;

public class Divide extends BinaryOperation {
    public Divide(PartOfExpression left, PartOfExpression right) {
        super(left, right, "/");
    }

    @Override
    public double evaluate(final double x) {
        return left.evaluate(x) / right.evaluate(x);
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return left.evaluate(x, y, z) / right.evaluate(x, y, z);
    }

    @Override
    public String toMiniString() {
        return buildMiniString(left.getClass() == Add.class || left.getClass() == Subtract.class,
                right.getClass() == Add.class || right.getClass() == Subtract.class
                        || right.getClass() == Divide.class || right.getClass() == Multiply.class);
    }
}
