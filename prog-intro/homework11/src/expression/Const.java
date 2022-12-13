package expression;

public class Const extends PartOfExpression {
    private int value;
    private double x;
    private final boolean isDouble;

    public Const(final int value) {
        super("");
        this.value = value;
        this.isDouble = false;
    }

    public Const(double x) {
        super("");
        this.x = x;
        this.isDouble = true;
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof Const) && (isDouble && ((Const) other).x == x || !isDouble && ((Const) other).value == value);
    }

    @Override
    public String toString() {
        if (isDouble) {
            return String.valueOf(x);
        }
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value) + Double.hashCode(x);
    }

    @Override
    public double evaluate(double x) {
        return this.x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }
}
