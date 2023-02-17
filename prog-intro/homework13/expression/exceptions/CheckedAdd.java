package expression.exceptions;

import expression.Add;
import expression.BinaryOperation;
import expression.PartOfExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(PartOfExpression left, PartOfExpression right) {
        super(left, right);
    }

    @Override
    protected int calculate(int x, int y) {
        int result = x + y;
        if (((x ^ result) & (y ^ result)) < 0) {
            throw new ArithmeticException("Overflow");
        }
        return result;
    }
}
