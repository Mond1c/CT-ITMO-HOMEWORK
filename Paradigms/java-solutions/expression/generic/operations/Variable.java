package expression.generic.operations;

import java.util.List;

import expression.generic.types.NumberType;

public class Variable<T extends Comparable<T>> extends AbstractTripleExpression<T> {
    private final String variable;

    public static final List<String> POSSIBLE_VARIABLE_NAMES = List.of("x", "y", "z");

    public Variable(String variable, NumberType<T> performer) {
        super(performer);
        this.variable = variable;
    }

    public static boolean isNameOfVariable(String s) {
        return POSSIBLE_VARIABLE_NAMES.contains(s); 
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (variable.toLowerCase()) {
            case "x":
                yield x;
            case "y":
                yield y;
            case "z":
                yield z;
            default : throw new IllegalArgumentException("Variable must have name x, y or z");
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable<?> other) { // :TODO:
            return other.variable.equals(this.variable);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public String toMiniString() {
        return variable;
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(variable);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }
}