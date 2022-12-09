package ExpressionParser;

import expression.Const;
import expression.Divide;
import expression.Expression;
import expression.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Parser {
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "(", ")");
    private final List<String> values;
    private final List<String> operators;
    private final String expression;

    public Parser(final String expression) {
        this.expression = expression;
        this.values = new ArrayList<>();
        this.operators = new ArrayList<>();
    }

    public Expression parse() {
        Tokenizer stringTokenizer = new Tokenizer(expression, "*/+-()");
        while (stringTokenizer.hasNextToken()) {
            String token = stringTokenizer.nextToken();
            if (OPERATORS.contains(token)) {
                operators.add(token);
            } else {
                values.add(token);
            }
        }
        int pointer = 0;
        while (pointer < operators.size()) {
   //         final Expression left =
        }
        return null;
    }

    private Expression parseValueToken(final String token) {
        if (Character.isDigit(token.charAt(0))) {
            return new Const(Integer.parseInt(token));
        }
        return new Variable(token);
    }
}
