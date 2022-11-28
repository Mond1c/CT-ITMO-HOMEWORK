package expresssion;

public class Const implements Expression {
    private final int value;

    public Const(final int value) {
        this.value = value;
    }

    @Override
    public int evaluate(final int value) {
        return this.value;
    }

    @Override
    public String toMiniString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(final Expression other) {
        return toString().equals(other.toString());
    }

    @Override
    public String getMiniString(Operation operation) {
        return toMiniString();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
