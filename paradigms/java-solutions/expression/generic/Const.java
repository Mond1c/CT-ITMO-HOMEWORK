package expression.generic;

import expression.generic.types.Type;

public class Const<T extends Number> implements PartOfExpression<T> {
    private final Type<T> value;

    public Const(Type<T> value) {
        this.value = value;
    }

    @Override
    public Type<T> evaluate(Type<T> x, Type<T> y, Type<T> z) {
        return value;
    }
}
