package expression.generic.types;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerNumberType implements NumberType<Integer> {
    @Override
    public Integer add(Integer left, Integer right) {
        if (left > 0 && right > 0 && left + right < 0 ||
            left < 0 && right < 0 && left + right >= 0) {
                throw new OverflowException(left + " + " + right);
        }
        return left + right;
    }

    @Override
    public Integer sub(Integer left, Integer right) {
        if (left >= 0 && right < 0 && left - right <= 0 ||
            left <= 0 && right > 0 && left - right >= 0) {
            throw new OverflowException(left + " - " + right); 
        } else {
            return left - right;
        }
    }

    @Override
    public Integer mul(Integer left, Integer right) {
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

    @Override
    public Integer div(Integer left, Integer right) {
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException(left + " / " + right); 
        } else if (right == 0) {
            throw new DivisionByZeroException(String.valueOf(left), String.valueOf(right)); 
        } else {
            return left / right;
        }
    }

    @Override
    public Integer mod(Integer left, Integer right) {
        // if (left == Integer.MIN_VALUE && right == -1) {
            // throw new OverflowException(left + " % " + right); 
        if (right == 0) {
            throw new DivisionByZeroException(String.valueOf(left), String.valueOf(right)); 
        } else {
            return left % right;
        }
    }

    @Override
    public Integer negate(Integer num) {
        if (num == Integer.MIN_VALUE) {
            throw new OverflowException("Negating minimum: " + num);
        } else {
            return -num;
        }
    }

    @Override
    public Integer valueOf(String unparsedConst) {
        return Integer.parseInt(unparsedConst);
    }

    @Override
    public Integer valueOf(int integerValue) {
        return integerValue;
    }

    @Override
    public Integer getZero() {
        return 0;
    }
}
