package expression;

public class SetBit extends BinaryOperation {

    public SetBit(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "set";
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    protected int calc(int left, int right) {
        return left |= (1 << right);
    }
}
