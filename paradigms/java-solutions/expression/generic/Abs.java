package expression.generic;

import expression.generic.types.Type;

public class Abs<T extends Number> extends UnaryOperation<T> {
    public Abs(PartOfExpression<T> part) {
        super(part);
    }

    @Override
    protected Type<T> calculate(Type<T> x) {
        return x.abs();
    }
}
