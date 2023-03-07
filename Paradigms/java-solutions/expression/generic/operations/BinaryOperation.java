package expression.generic.operations;

import java.util.Objects;

public abstract class BinaryOperation<T extends Comparable<T>> extends BaseOperation<T> {
    private final AbstractTripleExpression<T> left, right;

    public abstract boolean isCommutative();

    protected abstract T calc(T left, T right);

    public BinaryOperation(AbstractTripleExpression<T> left, AbstractTripleExpression<T> right) {
        super(left.getPerformer());
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + getOperationString() + " " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            BinaryOperation<?> bOperation = (BinaryOperation<?>) obj;
            return left.equals(bOperation.left) && right.equals(bOperation.right);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, getClass());
    } 

    @Override
    public void toMiniString(StringBuilder sb) {
        if (left.getPriority() > this.getPriority()) {
            appendWithBrackets(sb, left);
        } else {
            appendWithoutBrackets(sb, left);
        }
        sb.append(" ").append(this.getOperationString()).append(" ");
        if (right.getPriority() > this.getPriority() || 
                (right.getPriority() == this.getPriority() && 
                    (
                        !this.isCommutative() || 
                        right.needBracketsIfEqualPriority()
                    )
                )
            ) 
        {
            appendWithBrackets(sb, right);
        } else {
            appendWithoutBrackets(sb, right);
        }
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        toMiniString(sb);
        return sb.toString();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return this.calc(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}