package parser;

import expression.Add;
import expression.PartOfExpression;

public class Parser {
    private static PartOfExpression parseToPartOfExpression(final String token) {
        return switch (token) {
            case "+" -> new Add(token);
        }
    }
}