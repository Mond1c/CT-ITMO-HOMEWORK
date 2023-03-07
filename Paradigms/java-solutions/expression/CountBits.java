package expression;

public class CountBits extends UnaryOperation {

    public CountBits(MegaExpression expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "count";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }

    @Override
    protected int calc(int x) {
        return Integer.bitCount(x);
    }

    @Override
    protected double calc(double x) {
        // not implemented.
        return 0;
    }
    
}
