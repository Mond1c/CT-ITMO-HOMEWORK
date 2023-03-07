package expression;

public class Divide extends BinaryOperation {
    public Divide(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return true;
    }

    @Override
    protected int calc(int left, int right) {
        return left / right;
    }
}
