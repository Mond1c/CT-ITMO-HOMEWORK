package expression.exceptions;

import expression.UnaryOperation;
import expression.MegaExpression;

public class CheckedLog extends UnaryOperation {
    public CheckedLog(MegaExpression expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "log10";
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
        if (x <= 0) { 
            throw new NotValidArgument("log", String.valueOf(x)); 
        }; 
        int res = 0;
        while (x >= 10) {
            x /= 10;
            res++;
        }
        return res;
    }

    @Override
    protected double calc(double x) {
        // not implemented.
        return 0;
    }

}
