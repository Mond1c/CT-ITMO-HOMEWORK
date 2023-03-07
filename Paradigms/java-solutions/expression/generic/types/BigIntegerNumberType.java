package expression.generic.types;

import java.math.BigInteger;

public class BigIntegerNumberType implements NumberType<BigInteger> {
    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger sub(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger mul(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger div(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger mod(BigInteger left, BigInteger right) {
        return left.mod(right);
    }

    @Override
    public BigInteger negate(BigInteger num) {
        return num.negate();
    }

    @Override
    public BigInteger valueOf(String unparsedConst) {
        return new BigInteger(unparsedConst);
    }

    @Override
    public BigInteger valueOf(int integerValue) {
        return BigInteger.valueOf(integerValue);
    }

    @Override
    public BigInteger getZero() {
        return BigInteger.ZERO;
    }
}
