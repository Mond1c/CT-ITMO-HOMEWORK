package expression.exceptions;

import expression.BinaryOperation;
import expression.PartOfExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(PartOfExpression left, PartOfExpression right) {
        super(left, right);
    }

    @Override
    protected int calculate(int x, int y) {
        int result = x - y;
        if (((x ^ y) & (x ^ result)) < 0) {
            throw new ArithmeticException("Overflow");
        }
        return result;
    }
}
