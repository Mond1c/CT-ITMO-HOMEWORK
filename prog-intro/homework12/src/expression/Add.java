package expression;

public class Add extends BinaryOperation {
    public Add(PartOfExpression left, PartOfExpression right) {
        super(left, right, "+", 0);
    }

    public Add(PartOfExpression left, PartOfExpression right, final int additionPriority) {
        super(left, right, "+", additionPriority);
    }

    @Override
    public double evaluate(final double x) {
        return left.evaluate(x) + right.evaluate(x);
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return left.evaluate(x, y, z) + right.evaluate(x, y, z);
    }

    @Override
    public String toMiniString() {
        return buildMiniString(false, false);
    }
}
