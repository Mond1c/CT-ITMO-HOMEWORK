package expression.exceptions;

import expression.Add;
import expression.MegaExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    protected int calc(int left, int right){
        if (left > 0 && right > 0 && left + right < 0 ||
            left < 0 && right < 0 && left + right >= 0) {
            throw new OverflowException(left + " + " + right); 
        } else {
            return left + right;
        }
    }
}
