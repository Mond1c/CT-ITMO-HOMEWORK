package expression.exceptions;

import expression.Divide;
import expression.MegaExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    protected int calc(int left, int right) {
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException(left + " / " + right); 
        } else if (right == 0) {
            throw new DivisionByZeroException(String.valueOf(left), String.valueOf(right)); 
        } else {
            return left / right;
        }
    }
}
