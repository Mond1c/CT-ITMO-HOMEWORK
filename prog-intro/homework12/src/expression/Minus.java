package expression;

public class Minus extends PartOfExpression {
    private final PartOfExpression partOfExpression;

    public Minus(final PartOfExpression partOfExpression) {
        super("");
        this.partOfExpression = partOfExpression;
    }

    @Override
    protected String buildMiniString(boolean isBracketsNeededOnTheLeftSide, boolean isBracketsNeededOnTheRightSide) {
        if (partOfExpression instanceof Const || partOfExpression instanceof Variable || partOfExpression instanceof Minus) {
            return "- " + partOfExpression.toMiniString();
        }
        return "-(" + partOfExpression.toMiniString() + ")";
    }

    @Override
    public String toMiniString() {
        return buildMiniString(false, false);
    }

    @Override
    public String toString() {
        return "-(" + partOfExpression.toString() + ")";
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -partOfExpression.evaluate(x, y, z);
    }

    @Override
    public double evaluate(double x) {
        return -partOfExpression.evaluate(x);
    }
}
