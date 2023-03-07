package expression.generic.operations;

public class Divide<T extends Comparable<T>> extends BinaryOperation<T> {
    public Divide(AbstractTripleExpression<T> left, AbstractTripleExpression<T> right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 2;
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
    protected T calc(T left, T right) {
        return getPerformer().div(left, right);
    }
}
