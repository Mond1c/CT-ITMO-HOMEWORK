package expression.generic;

import expression.generic.types.Type;

public class Subtract<T extends Number> extends BinaryOperation<T> {

    public Subtract(PartOfExpression<T> left, PartOfExpression<T> right) {
        super(left, right);
    }

    @Override
    protected Type<T> calculate(Type<T> x, Type<T> y) {
        return x.subtract(y);
    }
}
