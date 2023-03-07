package expression;

public abstract class UnaryOperation extends BaseOperation {
    MegaExpression expr;

    protected abstract int calc(int x);
    protected abstract double calc(double x);

    public UnaryOperation(MegaExpression expr) {
        this.expr = expr;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(expr.evaluate(x, y, z));
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(getOperationString());
        if (expr instanceof UnaryOperation operation) {
            sb.append(' ');
            appendWithoutBrackets(sb, expr);
        }
        else if (expr instanceof Operation operation) {
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