package expression.generic;

import expression.generic.types.Type;

public class Negate<T extends Number> extends UnaryOperation<T> {
    public Negate(PartOfExpression<T> part) {
        super(part);
    }

    @Override
    protected Type<T> calculate(Type<T> x) {
        return x.negate();
    }
}
