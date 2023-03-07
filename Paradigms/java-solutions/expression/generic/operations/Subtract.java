package expression.generic.operations;

public class Subtract<T extends Comparable<T>> extends BinaryOperation<T> {
    public Subtract(AbstractTripleExpression<T> left, AbstractTripleExpression<T> right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }

    @Override
    protected T calc(T left, T right) {
        return getPerformer().sub(left, right);
    }
}
