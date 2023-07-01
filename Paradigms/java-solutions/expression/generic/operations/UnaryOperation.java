package expression.generic.operations;

public abstract class UnaryOperation<T extends Comparable<T>> extends BaseOperation<T> {
    TripleExpression<T> expr;

    protected abstract T calc(T x);

    public UnaryOperation(AbstractTripleExpression<T> expr) {
        super(expr.getPerformer());
        this.expr = expr;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(expr.evaluate(x, y, z));
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(getOperationString());
        if (expr instanceof UnaryOperation<T>) {
            sb.append(' ');
            appendWithoutBrackets(sb, expr);
        }
        else if (expr instanceof Operation<T> operation) {
            appendWithBrackets(sb, operation);
        } else {
            sb.append(' ');
            appendWithoutBrackets(sb, expr);
        }
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        toMiniString(sb);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getOperationString() + "(" + expr.toString() + ")";
    }
}