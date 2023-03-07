package expression.generic.operations;

import expression.generic.types.NumberType;

public class Const<T extends Comparable<T>> extends AbstractTripleExpression<T> {
    private final T value;

    public Const(String unparsedConst, NumberType<T> performer) {
        super(performer);
        value = performer.valueOf(unparsedConst);
    }

    public Const(T value, NumberType<T> performer) {
        super(performer);
        this.value = value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const<?> other) { 
            return this.value.equals(other.value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(this.value);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean needBracketsIfEqualPriority() {
        return false;
    }
}
