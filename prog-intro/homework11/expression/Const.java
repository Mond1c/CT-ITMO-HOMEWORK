package expression;

public class Const implements PartOfExpression {
    private final Number x;

    public Const(final int x) {
        this.x = x;
    }

    public Const(final double x) {
        this.x = x;
    }
 
    @Override
    public boolean equals(final Object other) {
        return (other instanceof Const c) && x.equals(c.x);
    }

    @Override
    public String toString() {
        return x.toString();
    }

    @Override
    public int hashCode() {
        return x.hashCode();
    }

    @Override
    public double evaluate(double x) {
        return this.x.doubleValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.x.intValue();
    }

    @Override
    public int evaluate(int x) {
        return this.x.intValue();
    }
}
