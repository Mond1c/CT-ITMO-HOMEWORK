package expression.generic;

import expression.ToMiniString;
import expression.generic.types.Type;

public interface PartOfExpression<T extends Number> extends ToMiniString {
    Type<T> evaluate(Type<T> x, Type<T> y, Type<T> z);
}
