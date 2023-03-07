package expression.generic.types;

public class UShortNumberType implements NumberType<Short> {
    @Override
    public Short add(Short left, Short right) {
        return (short) (left + right);
    }

    @Override
    public Short sub(Short left, Short right) {
        return (short) (left - right);
    }

    @Override
    public Short mul(Short left, Short right) {
        return (short) (left * right);
    }

    @Override
    public Short div(Short left, Short right) {
        return (short) (left / right);
    }

    @Override
    public Short mod(Short left, Short right) {
        return (short) (left % right);
    }

    @Override
    public Short negate(Short num) {
        return (short) -num;
    }

    @Override
    public Short valueOf(String unparsedConst) {
        return Short.parseShort(unparsedConst);
    }

    @Override
    public Short valueOf(int integerValue) {
        return (short) integerValue;
    }

    @Override
    public Short getZero() {
        return 0;
    }
}
