package expression.exceptions;

import expression.BinaryOperation;
import expression.Divide;
import expression.PartOfExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(PartOfExpression left, PartOfExpression right) {
        super(left, right);
    }


    @Override
    protected int calculate(int x, int y) {
        if (y == 0) {
            throw new ArithmeticException("Division by zero");
        } else if (x == Integer.MIN_VALUE && y == -1) {
            throw new ArithmeticException("Overflow");
        }
        return x / y;
    }
}
