package expression.generic.types;

public class DoubleType extends Type<Double> {


    public DoubleType(Double value) {
        super(value);
    }

    public static DoubleType parse(String str) {
        return new DoubleType(Double.parseDouble(str));
    }

    @Override
    protected Double addImpl(Double other) {
        return value + other;
    }

    @Override
    protected Double subtractImpl(Double other) {
        return value - other;
    }

    @Override
    protected Double divideImpl(Double other) {
        return value / other;
    }

    @Override
    protected Double multiplyImpl(Double other) {
        return value * other;
    }

    @Override
    protected Double modImpl(Double other) {
        return value % other;
    }

    @Override
    protected Double negateImpl() {
        return -value;
    }

    @Override
    protected Double absImpl() {
        return Math.abs(value);
    }


    @Override
    protected Type<Double> toType(Double value) {
        return new DoubleType(value);
    }
}
