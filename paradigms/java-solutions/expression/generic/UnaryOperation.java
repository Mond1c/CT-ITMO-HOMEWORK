package expression.generic;

import expression.generic.types.Type;

public abstract class UnaryOperation<T extends Number> implements PartOfExpression<T> {
    private final PartOfExpression<T> part;

    public UnaryOperation(PartOfExpression<T> part) {
        this.part = part;
    }

    protected abstract Type<T> calculate(Type<T> x);

    @Override
    public Type<T> evaluate(Type<T> x, Type<T> y, Type<T> z) {
        return calculate(part.evaluate(x, y, z));
    }
}
