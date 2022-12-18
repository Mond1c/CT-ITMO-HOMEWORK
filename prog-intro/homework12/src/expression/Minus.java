package expression;

public class Minus implements PartOfExpression {
    private final PartOfExpression partOfExpression;

    public Minus(final PartOfExpression partOfExpression) {
        this.partOfExpression = partOfExpression;
    }

    @Override
    public String toString() {
        return "-(" + partOfExpression.toMiniString() + ")";
    }

    @Override
    public String toMiniString() {
        if (partOfExpression instanceof Const || partOfExpression instanceof Variable) {
            return "- " + partOfExpression.toMiniString();
        }
        return "-(" + partOfExpression.toMiniString() + ")";
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -partOfExpression.evaluate(x, y, z);
    }

    @Override
    public double evaluate(double x) {
        return -partOfExpression.evaluate(x);
    }

    @Override
    public int evaluate(int x) {
        return -partOfExpression.evaluate(x);
    }
}
