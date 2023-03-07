package expression.generic.operations;

import expression.generic.types.NumberType;

public abstract class BaseOperation<T extends Comparable<T>> extends AbstractTripleExpression<T> implements Operation<T> {
    protected BaseOperation(NumberType<T> performer) {
        super(performer);
    }

    protected void appendWithBrackets(StringBuilder sb, TripleExpression<T> operation) {
        sb.append("(");
        operation.toMiniString(sb);
        sb.append(")");
    }

    protected void appendWithoutBrackets(StringBuilder sb, TripleExpression<T> operation) {
        operation.toMiniString(sb);
    }
}
