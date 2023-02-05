package expression.exceptions;

import expression.Log;
import expression.PartOfExpression;

public class CheckedLog extends Log {
    public CheckedLog(PartOfExpression part) {
        super(part);
    }

    @Override
    public int calculate(int x) {
        if (x <= 0) {
            throw new ArithmeticException("You can calculate log10 only with x greater than 0");
        }
        int ans = 0;
        while (x > 9) {
            ans++;
            x /= 10;
        }
        return ans;
    }
}
