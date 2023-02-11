package expression.exceptions;

import expression.Multiply;
import expression.PartOfExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(PartOfExpression left, PartOfExpression right) {
        super(left, right);
    }

    @Override
    protected int calculate(int x, int y) {
        int result = x * y;
        if (x == Integer.MIN_VALUE && y == -1 || x == -1 && y == Integer.MIN_VALUE
            || y != 0 && result / y != x) {
            throw new ArithmeticException("Overflow");
        }
        return result;
    }
}
