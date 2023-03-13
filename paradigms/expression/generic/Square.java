package expression.generic;

import expression.generic.types.Type;

public class Square<T extends Number> extends UnaryOperation<T> {
    public Square(PartOfExpression<T> part) {
        super(part);
    }

    @Override
    protected Type<T> calculate(Type<T> x) {
        return x.square();
    }
}
