package expression.exceptions;

import expression.Negate;
import expression.MegaExpression;

public class CheckedNegate extends Negate {
    public CheckedNegate(MegaExpression expr) {
        super(expr);
    }

    @Override
    protected int calc(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Negating minimum: " + String.valueOf(x)); 
        } else {
            return -x;
        }
    }
}