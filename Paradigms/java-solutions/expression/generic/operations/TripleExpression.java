package expression.generic.operations;

import expression.ToMiniStringExtended;

public interface TripleExpression<T> extends ToMiniStringExtended {
    T evaluate(T x, T y, T z);
}
