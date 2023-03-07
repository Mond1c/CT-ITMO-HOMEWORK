package expression.generic.operations;
public class Add<T extends Comparable<T>> extends BinaryOperation<T> {
    public Add(AbstractTripleExpression<T> left, AbstractTripleExpression<T> right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 3;
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
        return getPerformer().add(left, right);
    }
}