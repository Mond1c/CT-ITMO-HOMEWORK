package expression;

public class Variable extends PartOfExpression {
    private final String name;

    public Variable(final String name) {
        super("");
        this.name = name;
    }

    @Override
    public boolean equals(final Object other) {
        // :NOTE: toString
        return (other instanceof Variable) && ((Variable) other).name.equals(name);
    }
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public double evaluate(final double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("Variable name is not available");
        };
    }
}
