package expression.generic.operations;

public class Mod<T extends Comparable<T>> extends BinaryOperation<T> {

    public Mod(AbstractTripleExpression<T> left, AbstractTripleExpression<T> right) {
        super(left, right);
    }

    @Override
    public String getOperationString() {
        return "mod";
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    protected T calc(T left, T right) {
        return getPerformer().mod(left, right);
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return true;
    }

    @Override
    public int getPriority() {
        return 1;
    }
    
}
