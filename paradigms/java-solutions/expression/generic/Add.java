package expression.generic;

import expression.generic.types.Type;

public class Add<T extends Number> extends BinaryOperation<T> {
    public Add(PartOfExpression<T> left, PartOfExpression<T> right) {
        super(left, right);
    }

    @Override
    protected Type<T> calculate(Type<T> x, Type<T> y) {
        return x.add(y);
    }


}
