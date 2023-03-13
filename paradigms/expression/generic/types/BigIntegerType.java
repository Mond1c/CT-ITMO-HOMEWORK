package expression.generic.types;

import java.math.BigInteger;

public class BigIntegerType extends Type<BigInteger> {

    public BigIntegerType(BigInteger value) {
        super(value);
    }

    public static BigIntegerType parse(String str) {
        return new BigIntegerType(new BigInteger(str));
    }

    @Override
    protected BigInteger addImpl(BigInteger other) {
        return value.add(other);
    }

    @Override
    protected BigInteger subtractImpl(BigInteger other) {
        return value.subtract(other);
    }

    @Override
    protected BigInteger divideImpl(BigInteger other) {
        return value.divide(other);
    }

    @Override
    protected BigInteger multiplyImpl(BigInteger other) {
        return value.multiply(other);
    }

    @Override
    protected BigInteger modImpl(BigInteger other) {
        return value.mod(other);
    }

    @Override
    protected BigInteger negateImpl() {
        return value.negate();
    }

    @Override
    protected BigInteger absImpl() {
        return value.abs();
    }


    @Override
    protected Type<BigInteger> toType(BigInteger value) {
        return new BigIntegerType(value);
    }
}
