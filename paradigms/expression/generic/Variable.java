package expression.generic;

import expression.generic.types.Type;

public class Variable<T extends Number> implements PartOfExpression<T> {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Type<T> evaluate(Type<T> x, Type<T> y, Type<T> z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("Variable name is not available");
        };
    }
}
