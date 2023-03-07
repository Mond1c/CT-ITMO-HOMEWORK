package expression;

public class ClearBit extends BinaryOperation {

    public ClearBit(MegaExpression left, MegaExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "clear";
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
        return (left & (1 << right)) == 0 ? left : left - (1 << right);
    }
}
