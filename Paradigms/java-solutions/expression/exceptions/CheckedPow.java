package expression.exceptions;

import expression.UnaryOperation;
import expression.MegaExpression;

public class CheckedPow extends UnaryOperation {
    public CheckedPow(MegaExpression expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "pow10";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }

    @Override
    protected int calc(int x) {
        if (x > 9) { throw new OverflowException("pow10 " + x); };
        if (x < 0) { throw new NotValidArgument("pow", String.valueOf(x)); }
        int res = 1;
        while (x > 0) {
            res *= 10;
            x--;
        }
        return res;
    }

    @Override
    protected double calc(double x) {
        return 0;
    }
}
