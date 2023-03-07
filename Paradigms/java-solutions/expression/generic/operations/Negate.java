package expression.generic.operations;

public class Negate<T extends Comparable<T>> extends UnaryOperation<T> {
    public Negate(AbstractTripleExpression<T> expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return true;
    }

    @Override
    protected T calc(T x) {
        return getPerformer().negate(x);
    }
}
