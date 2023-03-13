package expression.generic.types;

public class ShortType extends Type<Short> {
    public ShortType(Short value) {
        super(value);
    }

    public static ShortType parse(String str) {
        return new ShortType((short)Integer.parseInt(str));
    }

    @Override
    protected Short addImpl(Short other) {
        return (short) (value + other);
    }

    @Override
    protected Short subtractImpl(Short other) {
        return (short) (value - other);
    }

    @Override
    protected Short divideImpl(Short other) {
        return (short) (value / other);
    }

    @Override
    protected Short multiplyImpl(Short other) {
        return (short) (value * other);
    }

    @Override
    protected Short modImpl(Short other) {
        return (short) (value % other);
    }

    @Override
    protected Short negateImpl() {
        return (short) -value;
    }

    @Override
    protected Short absImpl() {
        return (short) Math.abs(value);
    }

    @Override
    protected Type<Short> toType(Short value) {
        return new ShortType(value);
    }
}
