package expression;
public class Add extends BinaryOperation {
    public Add(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }

    @Override
    protected int calc(int left, int right) {
        return left + right;
    }
}