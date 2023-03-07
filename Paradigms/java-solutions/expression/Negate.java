package expression;

public class Negate extends UnaryOperation {
    public Negate(MegaExpression expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return true;
    }

    @Override
    protected int calc(int x) {
        return -x;
    }

    @Override
    protected double calc(double x) {
        return -x;
    }

}