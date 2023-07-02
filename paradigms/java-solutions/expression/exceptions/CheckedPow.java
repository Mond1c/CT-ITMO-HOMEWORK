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
        if (x >= 10) {
            throw new ArithmeticException("Overflow");
        }
       // double result = Math.pow(10, x);  // :NOTE:
        int result = 1;
        while (x-- > 0) {
            result *= 10;
        }
        return result;
    }
}
