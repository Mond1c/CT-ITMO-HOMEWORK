package ExpressionParser;

import expression.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Parser {
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "(", ")");
    private final Stack<String> values;
    private final Stack<String> operators;
    private final Stack<PartOfExpression> st;
    private final String expression;

    public Parser(final String expression) {
        this.expression = expression;
        this.values = new Stack<>();
        this.operators = new Stack<>();
        this.st = new Stack<>();
    }

    public Expression parse() {
        Tokenizer stringTokenizer = new Tokenizer(expression, "*/+-()");
        while (stringTokenizer.hasNextToken()) {
            String token = stringTokenizer.nextToken();
            if (OPERATORS.contains(token)) {
                operators.push(token);
            } else {
                values.push(token);
            }
        }
        while (!operators.isEmpty()) {
            final PartOfExpression left = parseValueToken(values.pop());
            final PartOfExpression right = parseValueToken(values.pop());
            final BinaryOperation operation = parseOperation(operators.pop(), left, right);
            st.push(operation);
        }
        return st.top();
    }

    private PartOfExpression parseValueToken(final String token) {
        if (Character.isDigit(token.charAt(0))) {
            return new Const(Integer.parseInt(token));
        }
        return new Variable(token);
    }

    private BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+": yield new Add(left, right);
            case "-": yield new Subtract(left, right);
            case "*": yield new Multiply(left, right);
            case "/": yield new Divide(left, right);
            default: yield null;
        };
    }
}
