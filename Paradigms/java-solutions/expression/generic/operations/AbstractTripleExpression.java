package expression.generic.operations;

import expression.generic.types.NumberType;

public abstract class AbstractTripleExpression<T extends Comparable<T>> implements TripleExpression<T> {
    private final NumberType<T> performer;

    protected AbstractTripleExpression(NumberType<T> performer) {
        this.performer = performer;
    }

    public NumberType<T> getPerformer() {
        return performer;
    }

    public abstract boolean needBracketsIfEqualPriority();
    public abstract int getPriority();
}
