package expression.exceptions;

import expression.Negate;
import expression.PartOfExpression;
import expression.UnaryOperation;

public class CheckedNegate extends Negate {
    public CheckedNegate(PartOfExpression part) {
        super(part);
    }

    @Override
    protected int calculate(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return -x;
    }
}
