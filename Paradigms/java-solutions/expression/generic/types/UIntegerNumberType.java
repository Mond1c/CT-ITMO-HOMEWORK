package expression.generic.types;

public class UIntegerNumberType implements NumberType<Integer> {
    @Override
    public Integer add(Integer left, Integer right) {
        return left + right;
    }

    @Override
    public Integer sub(Integer left, Integer right) {
        return left - right;
    }

    @Override
    public Integer mul(Integer left, Integer right) {
        return left * right;
    }

    @Override
    public Integer div(Integer left, Integer right) {
        return left / right;
    }

    @Override
    public Integer mod(Integer left, Integer right) {
        return left % right;
    }

    @Override
    public Integer negate(Integer num) {
        return -num;
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
