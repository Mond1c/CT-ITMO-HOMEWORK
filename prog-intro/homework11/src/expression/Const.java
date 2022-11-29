package expression;

    public class Const extends PartOfExpression {
    private final int value;

    public Const(final int value) {
        super(Operation.NONE);
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
    public boolean equals(final Object other) {
        return other != null && toString().equals(other.toString());
    }

    @Override
    public String getMiniString(boolean isBracketsNeeded) {
        return toMiniString();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
