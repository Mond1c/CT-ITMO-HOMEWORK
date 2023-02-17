package expression;

public class Log extends UnaryOperation {
    public Log(PartOfExpression part) {
        super("log10", 3, part);
    }

    @Override
    protected int calculate(int x) {
        int ans = 0;
        while (x > 9) {
            ans++;
            x /= 10;
        }
        return ans;
    }

    @Override
    protected double calculate(double x) {
        throw new ArithmeticException("You can't calculate log10(x) where x is double");
    }
}
