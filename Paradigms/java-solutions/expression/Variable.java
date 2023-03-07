package expression;

import java.util.List;

public class Variable implements MegaExpression {
    private final String variable;
    public static final List<String> possibleVariableNames = List.of("x", "y", "z");

    public Variable(String variable) {
        this.variable = variable;
    }

    public static boolean isNameOfVariable(String s) {
        return possibleVariableNames.contains(s); 
    }

    @Override
    public int evaluate(int x, int y, int z) {
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
        if (obj instanceof Variable other) {
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