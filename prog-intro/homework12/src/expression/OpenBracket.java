package expression;

public class OpenBracket extends BinaryOperation {
    public OpenBracket() {
        super("(");
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return 0;
    }
}
