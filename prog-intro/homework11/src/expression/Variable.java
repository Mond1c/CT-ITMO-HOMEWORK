package expression;

public class Variable extends PartOfExpression {
    private final String variableName;

    public Variable(final String variableName) {
        super(Operation.NONE);
        this.variableName = variableName;
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        return other != null && toString().equals(other.toString());
    }

    @Override
    public String toMiniString() {
        return variableName;
    }

    @Override
    public String getMiniString(boolean isBracketsNeeded) {
        return toMiniString();
    }

    @Override
    public String toString() {
        return variableName;
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (char ch : variableName.toCharArray()) {
            code += Character.hashCode(ch);
        }
        return code;
    }
}
