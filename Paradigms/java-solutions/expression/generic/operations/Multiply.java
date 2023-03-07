package expression.generic.operations;

public class Multiply<T extends Comparable<T>> extends BinaryOperation<T> {
    public Multiply(AbstractTripleExpression<T> left, AbstractTripleExpression<T> right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "*";
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
    protected T calc(T left, T right) {
        return getPerformer().mul(left, right);
    }
}
