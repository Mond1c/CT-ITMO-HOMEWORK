package expression.generic;

import expression.exceptions.UnsupportedCharacterException;
import expression.generic.types.Type;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ExpressionParser<T extends Number> extends BaseParser {
    private final static List<Character> SUPPORTED_BEGIN_OF_OPERATIONS = List.of('+', '-', '*', '/', 'c', 's', 'm');
    private String expression;

    private final Function<String, Type<T>> constParser;

    public ExpressionParser(final Function<String, Type<T>> constParser) {
        this.constParser = constParser;
    }


    public PartOfExpression<T> parse(String expression)  {
        setSource(new StringSource(expression));
        this.expression = expression;
        final PartOfExpression<T> expr = parseExpression();
        if (!eof()) {
            if (take(')')) {
                throw new NoSuchElementException(getMessageForException("No opening parenthesis", '('));
            }
            throw new UnsupportedCharacterException(getMessageForException("Unsupported character", ch));
        }
        return expr;
    }

    private String getMessageForException(final String message, char ch) {
        int index = expression.indexOf(ch);
        if (eof() && index == -1) {
            index = expression.length() - 1;
        } else if (index == -1) {
            index = 0;
        }
        return message + " " + ch + ": " + expression.substring(Math.max(0, index - 10), index)
                + " --> " + ch + " <-- " + expression.substring(index + 1, Math.min(index + 10, expression.length()))
                + " at position " + index;
    }


    private PartOfExpression<T> parseConstVariablesUnaryOperationsParenthesis()  {
        skipWhitespaces();
        if (take('(')) {
            PartOfExpression<T> part = parseExpression();
            if (!take(')')) {
                throw new NoSuchElementException(getMessageForException("No closing parenthesis", ')'));
            }
            return part;
        } else if (between('0', '9')){
            return parseConst(false);
        } else if (between('x', 'z')) {
            return parseVariable();
        } else if (take('-')) {
            if (between('0', '9')) {
                return parseConst(true);
            }
            return new Negate<>(parseConstVariablesUnaryOperationsParenthesis());
        } else if (take("abs")) {
            return new Abs<>(parseConstVariablesUnaryOperationsParenthesis());
        } else if (take("square")) {
            return new Square<>(parseConstVariablesUnaryOperationsParenthesis());
        }
        throw new NoSuchElementException(getMessageForException("No argument", ch));
    }

    private PartOfExpression<T> parseExpression() {
        return parsePlusMinus();
    }


    private boolean isUnsupportedCharacter() {
        return !(between('x', 'z') || between('0', '9') || SUPPORTED_BEGIN_OF_OPERATIONS.contains(ch) || ch == ')');
    }

    private PartOfExpression<T>  parsePlusMinus() {
        PartOfExpression<T>  part = parseMulDiv();
        skipWhitespaces();
        // System.out.println(ch);
        if (isUnsupportedCharacter() && !eof()) {
            throw new UnsupportedCharacterException(getMessageForException("Unsupported character", ch));
        }
        while (test('+') || test('-')) { // z + y - -30 + (z + x)
            part = parseOperation(String.valueOf(take()), part, parseMulDiv());
        }
        return part;
    }

    private PartOfExpression<T>  parseMulDiv() {
        PartOfExpression<T>  part = parseConstVariablesUnaryOperationsParenthesis();
        skipWhitespaces();
        if (isUnsupportedCharacter() && !eof()) {
            throw new UnsupportedCharacterException(getMessageForException("Unsupported character", ch));
        }
        while (true) {
            String op;
            if (take('*')) {
                op = "*";
            } else if (take('/')) {
                op = "/";
            } else if (take("mod")) {
                op = "mod";
            } else {
                break;
            }
            part = parseOperation(op, part, parseConstVariablesUnaryOperationsParenthesis());
            skipWhitespaces();
        }
        return part;
    }


    private Variable<T> parseVariable() {
        return new Variable<>(String.valueOf(take()));
    }

    private Const<T> parseConst(boolean minus) {
        final StringBuilder builder = new StringBuilder();
        if (minus) {
            builder.append('-');
        }
        while (between('0', '9')) {
            builder.append(take());
        }
        skipWhitespaces();
        if (between('0', '9')) {
            throw new NumberFormatException(getMessageForException("Spaces in number", ch));
        }
        return new Const<>(constParser.apply(builder.toString()));
    }

    private BinaryOperation<T> parseOperation(final String operation, final PartOfExpression<T>  left, final PartOfExpression<T>  right) {
        return switch (operation) {
            case "+" -> new Add<>(left, right);
            case "-" -> new Subtract<>(left, right);
            case "*" -> new Multiply<>(left, right);
            case "/" ->  new Divide<>(left, right);
            case "mod" -> new Mod<>(left, right);
            default -> throw new UnsupportedOperationException(getMessageForException("Unsupported operation " + operation, ch));
        };
    }
}
