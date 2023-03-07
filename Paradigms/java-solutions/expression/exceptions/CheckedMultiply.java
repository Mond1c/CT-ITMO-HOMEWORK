package expression.exceptions;

import expression.Multiply;
import expression.MegaExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    protected int calc(int left, int right) {
        if (left > 0 && right > 0 && left > Integer.MAX_VALUE / right ||
            left < 0 && right < 0 && right < Integer.MAX_VALUE / left ||
            left < 0 && right > 0 && left < Integer.MIN_VALUE / right ||
            left > 0 && right < 0 && right < Integer.MIN_VALUE / left
            ) {
            throw new OverflowException(left + " * " + right); 
        } else {
            return left * right;
        }
    }

}
