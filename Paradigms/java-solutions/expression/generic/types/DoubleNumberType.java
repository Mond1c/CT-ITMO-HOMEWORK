package expression.generic.types;

public class DoubleNumberType implements NumberType<Double> {
    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double sub(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double mul(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double div(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double mod(Double left, Double right) {
        return left % right;
    }

    @Override
    public Double negate(Double num) {
        return -num;
    }

    @Override
    public Double valueOf(String unparsedConst) {
        return Double.parseDouble(unparsedConst);
    }

    @Override
    public Double valueOf(int integerValue) {
        return (double) integerValue;
    }

    @Override
    public Double getZero() {
        return 0.0;
    }
}