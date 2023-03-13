package expression.generic.types;

public class UnsafeIntegerType extends Type<Integer> {
    public UnsafeIntegerType(Integer value) {
        super(value);
    }

    public static UnsafeIntegerType parse(String str) {
        return new UnsafeIntegerType(Integer.parseInt(str));
    }

    @Override
    protected Integer addImpl(Integer other) {
        return value + other;
    }

    @Override
    protected Integer subtractImpl(Integer other) {
        return value - other;
    }

    @Override
    protected Integer divideImpl(Integer other) {
        return value / other;
    }

    @Override
    protected Integer multiplyImpl(Integer other) {
        return value * other;
    }

    @Override
    protected Integer modImpl(Integer other) {
        return value % other;
    }

    @Override
    protected Integer negateImpl() {
        return -value;
    }

    @Override
    protected Integer absImpl() {
        return Math.abs(value);
    }

    @Override
    protected Type<Integer> toType(Integer value) {
        return new UnsafeIntegerType(value);
    }
}
