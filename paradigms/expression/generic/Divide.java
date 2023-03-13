package expression.generic;

import expression.generic.types.Type;

public class Divide<T extends Number> extends BinaryOperation<T> {


    public Divide(PartOfExpression<T> left, PartOfExpression<T> right) {
        super(left, right);
    }

    @Override
    protected Type<T> calculate(Type<T> x, Type<T> y) {
        return x.divide(y);
    }
}
