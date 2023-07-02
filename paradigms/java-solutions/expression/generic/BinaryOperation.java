package expression.generic;

import expression.generic.types.Type;

public abstract class BinaryOperation<T extends Number> implements PartOfExpression<T> {
    protected final PartOfExpression<T> left;
    protected final PartOfExpression<T> right;

    public BinaryOperation(PartOfExpression<T> left, PartOfExpression<T> right) {
        this.left = left;
        this.right = right;
    }

    protected abstract Type<T> calculate(Type<T> x, Type<T> y);

    @Override
    public Type<T> evaluate(Type<T> x, Type<T> y, Type<T> z) {
        return calculate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
