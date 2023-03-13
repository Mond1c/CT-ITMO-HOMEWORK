package expression.generic;

import expression.generic.types.Type;

public class Multiply<T extends Number> extends BinaryOperation<T> {

    public Multiply(PartOfExpression<T> left, PartOfExpression<T> right) {
        super(left, right);
    }

    @Override
    protected Type<T> calculate(Type<T> x, Type<T> y) {
        return x.multiply(y);
    }


}
