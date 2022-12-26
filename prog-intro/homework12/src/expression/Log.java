package expression;

public class Log extends UnaryOperation {
    public Log(PartOfExpression part) {
        super("log10", 3, part);
    }

    @Override
    protected int calculate(int x) {
        return (int) Math.log10(x);
    }

    @Override
    protected double calculate(double x) {
        return Math.log10(x);
    }
}
