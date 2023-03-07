package expression.generic.parser;

import expression.generic.operations.TripleExpression;
import expression.generic.types.NumberType;

public interface TripleParser<T extends Comparable<T>> {
    TripleExpression<T> parse(String expression, NumberType<T> performer) throws Exception;
}
