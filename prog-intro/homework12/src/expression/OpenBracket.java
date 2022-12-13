package expression;

import expression.PartOfExpression;

public class OpenBracket extends BinaryOperation {
    public OpenBracket() {
        super("(");
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    protected String buildMiniString(boolean isBracketsNeededOnTheLeftSide, boolean isBracketsNeededOnTheRightSide) {
        return null;
    }

    @Override
    public String toMiniString() {
        return super.toMiniString();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return 0;
    }
}
