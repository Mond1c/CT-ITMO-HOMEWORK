package expression;

public class Pow extends UnaryOperation {
    public Pow(PartOfExpression part) {
        super("pow10", 3, part);
    }

    @Override
    protected int calculate(int x) {
        int result = 1;
        while (x-- > 0) {
            result *= 10;
        }
        return result;
    }

    @Override
    protected double calculate(double x) {
        throw new ArithmeticException("You can't calculate pow10(x) where x is double");
    }
}
