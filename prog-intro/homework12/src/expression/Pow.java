package expression;

public class Pow extends UnaryOperation {
    public Pow(PartOfExpression part) {
        super("pow10", 3, part);
    }

    @Override
    protected int calculate(int x) {
        return (int)Math.pow(10, x);
    }

    @Override
    protected double calculate(double x) {
        return Math.pow(10, x);
    }
}
