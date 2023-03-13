package expression.generic.types;

public class IntegerType extends Type<Integer> {
    public IntegerType(Integer value) {
        super(value);
    }

    public static IntegerType parse(String str) {
        return new IntegerType(Integer.parseInt(str));
    }

    @Override
    protected Integer addImpl(Integer other) {
        int result = value + other;
        if (((value ^ result) & (other ^ result)) < 0) {
            throw new ArithmeticException("Overflow");
        }
        return result;
    }

    @Override
    protected Integer subtractImpl(Integer other) {
        int result = value - other;
        if (((value ^ other) & (value ^ result)) < 0) {
            throw new ArithmeticException("Overflow");
        }
        return result;
    }

    @Override
    protected Integer divideImpl(Integer other) {
        if (other == 0) {
            throw new ArithmeticException("Division by zero");
        } else if (value == Integer.MIN_VALUE && other == -1) {
            throw new ArithmeticException("Overflow");
        }
        return value / other;
    }

    @Override
    protected Integer multiplyImpl(Integer other) {
        int result = value * other;
        if (value == Integer.MIN_VALUE && other == -1 || value == -1 && other == Integer.MIN_VALUE
                || other != 0 && result / other != value) {
            throw new ArithmeticException("Overflow");
        }
        return result;
    }

    @Override
    protected Integer modImpl(Integer other) {
        if (other == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return value % other;
    }

    @Override
    protected Integer negateImpl() {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return -value;
    }

    @Override
    protected Integer absImpl() {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return Math.abs(value);
    }

    @Override
    protected Type<Integer> toType(Integer value) {
        return new IntegerType(value);
    }
}
