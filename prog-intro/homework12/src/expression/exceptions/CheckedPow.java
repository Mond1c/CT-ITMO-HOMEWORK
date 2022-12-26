package expression.exceptions;

import expression.PartOfExpression;
import expression.Pow;

public class CheckedPow extends Pow {
    public CheckedPow(PartOfExpression part) {
        super(part);
    }

    @Override
    public int calculate(int x) {
        if (x < 0) {
            throw new ArithmeticException("You can calculate log10 only with x greater than -1");
        }
        double result = Math.pow(10, x);  // :NOTE:
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return (int) result;
    }
}
