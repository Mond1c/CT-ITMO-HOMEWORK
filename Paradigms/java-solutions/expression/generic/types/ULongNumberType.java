package expression.generic.types;

public class ULongNumberType implements NumberType<Long> {
    @Override
    public Long add(Long left, Long right) {
        return left + right;
    }

    @Override
    public Long sub(Long left, Long right) {
        return left - right;
    }

    @Override
    public Long mul(Long left, Long right) {
        return left * right;
    }

    @Override
    public Long div(Long left, Long right) {
        return left / right;
    }

    @Override
    public Long mod(Long left, Long right) {
        return left % right;
    }

    @Override
    public Long negate(Long num) {
        return -num;
    }

    @Override
    public Long valueOf(String unparsedConst) {
        return Long.parseLong(unparsedConst);
    }

    @Override
    public Long valueOf(int integerValue) {
        return (long) integerValue;
    }

    @Override
    public Long getZero() {
        return 0l;
    }
}
