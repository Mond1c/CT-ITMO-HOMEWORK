package expresssion;

public class Variable implements Expression {
    private final String variableName;

    public Variable(final String variableName) {
        this.variableName = variableName;
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    @Override
    public boolean equals(final Expression other) {
        return toString().equals(other.toString());
    }

    @Override
    public String toMiniString() {
        return variableName;
    }

    @Override
    public String getMiniString(Operation operation) {
        return toMiniString();
    }

    @Override
    public String toString() {
        return variableName;
    }
}
