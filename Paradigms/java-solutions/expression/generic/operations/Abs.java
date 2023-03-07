package expression.generic.operations;

public class Abs<T extends Comparable<T>> extends UnaryOperation<T>{

    public Abs(AbstractTripleExpression<T> expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "abs";
    }

    @Override
    protected T calc(T x) {
        return x.compareTo(getPerformer().getZero()) >= 0 ? x : getPerformer().negate(x);
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }
    
}
