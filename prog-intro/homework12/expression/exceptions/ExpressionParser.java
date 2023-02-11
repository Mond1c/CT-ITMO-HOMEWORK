package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;
import expression.parser.TripleParser;

import java.util.List;
import java.util.NoSuchElementException;

public class ExpressionParser extends BaseParser implements TripleParser {
    private final static List<Character> SUPPORTED_BEGIN_OF_OPERATIONS = List.of('+', '-', '*', '/', 'c', 's');
    private String expression;


    @Override
    public TripleExpression parse(String expression)  {
        setSource(new StringSource(expression));
        this.expression = expression;
        final TripleExpression expr = parseExpression();
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

    private RuntimeException throwAfterUnaryOperation(final String operation) {
        return new IllegalArgumentException(getMessageForException("You need to use whitespace or ( after unary operation " + operation, ch));
    }

    private RuntimeException throwUnsupportedOperation(final String operation) {
        return new UnsupportedOperationException(getMessageForException("No such operation " + operation, ch));
    }

    private RuntimeException throwUnsupportedCharacter() {
        return new UnsupportedCharacter(getMessageForException("Unsupported character", ch));
    }


    private PartOfExpression parseConstVariablesUnaryOperationsParenthesis()  {
        skipWhitespaces();
        if (take('(')) {
            PartOfExpression part = parseExpression();
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
            return new CheckedNegate(parseConstVariablesUnaryOperationsParenthesis());
        } else if (take("count")) {
            if (!Character.isWhitespace(ch) && ch != '(') {
                throw throwAfterUnaryOperation("count");
            }
            return new Count(parseConstVariablesUnaryOperationsParenthesis());
        } else if (take("pow10")) {
            if (!Character.isWhitespace(ch) && ch != '(') {
                throw throwAfterUnaryOperation("pow10");
            }
            return new CheckedPow(parseConstVariablesUnaryOperationsParenthesis());
        } else if (take("log10")) {
            if (!Character.isWhitespace(ch) && ch != '(') {
                throw throwAfterUnaryOperation("log10");
            }
            return new CheckedLog(parseConstVariablesUnaryOperationsParenthesis());
        }
        throw new NoSuchElementException(getMessageForException("No argument", ch));
    }

    private PartOfExpression parseExpression() {
        return parseSetClear();
    }

    private PartOfExpression parseSetClear() {
        skipWhitespaces();
        PartOfExpression part = parsePlusMinus();
        while (true) {
            if (take("set")) {
                if (!Character.isWhitespace(ch) && ch != '(' && ch != '-') {
                    throw throwUnsupportedOperation("set");
                }
                part = parseOperation("set", part, parsePlusMinus());
            } else if (take("clear") ) {
                if (!Character.isWhitespace(ch) && ch != '(' && ch != '-') {
                    throw throwUnsupportedOperation("clear");
                }
                part = parseOperation("clear", part, parsePlusMinus());
            } else {
                break;
            }
        }
        return part;
    }

    private boolean isUnsupportedCharacter() {
        return !(between('x', 'z') || between('0', '9') || SUPPORTED_BEGIN_OF_OPERATIONS.contains(ch) || ch == ')');
    }

    private PartOfExpression parsePlusMinus() {
        PartOfExpression part = parseMulDiv();
        skipWhitespaces();
        // System.out.println(ch);
        if (isUnsupportedCharacter() && !eof()) {
            throw throwUnsupportedCharacter();
        }
        while (test('+') || test('-')) { // z + y - -30 + (z + x)
            part = parseOperation(String.valueOf(take()), part, parseMulDiv());
        }
        return part;
    }

    private PartOfExpression parseMulDiv() {
        PartOfExpression part = parseConstVariablesUnaryOperationsParenthesis();
        skipWhitespaces();
        if (isUnsupportedCharacter() && !eof()) {
            throw throwUnsupportedCharacter();
        }
        while (test('*') || test('/')) {
            part = parseOperation(String.valueOf(take()), part, parseConstVariablesUnaryOperationsParenthesis());
            skipWhitespaces();
        }
        return part;
    }


    private Variable parseVariable() {
        return new Variable(String.valueOf(take()));
    }

    private Const parseConst(boolean minus) {
        final StringBuilder builder = new StringBuilder();
        if (minus) {
            builder.append('-');
        }
        while (between('0', '9')) {
            builder.append(take());
        }
        final int value;
        try {
            value = Integer.parseInt(builder.toString());
        } catch (NumberFormatException e) {
            throw new ArithmeticException("Overflow");
        }
        skipWhitespaces();
        if (between('0', '9')) {
            throw new NumberFormatException(getMessageForException("Spaces in number", ch));
        }
        return new Const(value);
    }

    private BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+" -> new CheckedAdd(left, right);
            case "-" -> new CheckedSubtract(left, right);
            case "*" -> new CheckedMultiply(left, right);
            case "/" -> new CheckedDivide(left, right);
            case "set" -> new Set(left, right);
            case "clear" -> new Clear(left, right);
            default -> throw throwUnsupportedOperation(operation);
        };
    }
}



