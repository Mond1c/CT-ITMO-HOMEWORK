package expression.generic.types;

public class LongType extends Type<Long> {
    public LongType(Long value) {
        super(value);
    }

    public static LongType parse(String str) {
        return new LongType(Long.parseLong(str));
    }

    @Override
    protected Long addImpl(Long other) {
        return value + other;
    }

    @Override
    protected Long subtractImpl(Long other) {
        return value - other;
    }

    @Override
    protected Long divideImpl(Long other) {
        return value / other;
    }

    @Override
    protected Long multiplyImpl(Long other) {
        return value * other;
    }

    @Override
    protected Long modImpl(Long other) {
        return value % other;
    }

    @Override
    protected Long negateImpl() {
        return -value;
    }

    @Override
    protected Long absImpl() {
        return Math.abs(value);
    }

    @Override
    protected Type<Long> toType(Long value) {
        return new LongType(value);
    }
}
