package expression.generic.operations;

public class Square<T extends Comparable<T>> extends UnaryOperation<T>{

    public Square(AbstractTripleExpression<T> expr) {
        super(expr);
    }

    @Override
    public String getOperationString() {
        return "square";
    }

    @Override
    protected T calc(T x) {
        return getPerformer().mul(x, x);
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
