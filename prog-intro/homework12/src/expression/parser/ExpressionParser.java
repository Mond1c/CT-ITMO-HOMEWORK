package expression.parser;

import expression.*;
import expression.exceptions.*;

import java.util.List;
import java.util.NoSuchElementException;

public class ExpressionParser extends BaseParser implements TripleParser {
    private final static boolean IS_CHECKED = false;
    private final static List<Character> SUPPORTED_BEGIN_OF_OPERATIONS = List.of('+', '-', '*', '/', 'c', 's');
    private String expression;


    @Override
    public TripleExpression parse(String expression)  {
        setSource(new StringSource(expression));
        this.expression = expression;
        final TripleExpression expr = parseExpression();
        if (!eof()) {
            if (take(')')) {
                throw new NoSuchElementException("No opening parenthesis");
            }
            throw new UnsupportedCharacter("Unsupported character " + ch + " in " + expression);
        }
        return expr;
    }


    private PartOfExpression parseStart()  {
        skipWhitespaces();
        if (take('(')) {
            PartOfExpression part = parseExpression();
            if (!take(')')) {
                throw new NoSuchElementException("No closing parenthesis in " + expression);
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
            return IS_CHECKED ? new CheckedNegate(parseStart()) : new Negate(parseStart());
        } else if (take('c')) {
            expect("ount");
            if (!Character.isWhitespace(ch) && ch != '(') {
                throw new UnsupportedOperation("No such operation " + "count" + ch + " in " + expression);
            }
            return new Count(parseStart());
        } else if (take('p')) {
            expect("ow10");
            if (!Character.isWhitespace(ch) && ch != '(') {
                throw new UnsupportedOperation("No such operation " + "pow10" + ch + " in " + expression);
            }
            return IS_CHECKED ? new CheckedPow(parseStart()) : new Pow(parseStart());
        } else if (take('l')) {
            expect("og10");
            if (!Character.isWhitespace(ch) && ch != '(') {
                throw new UnsupportedOperation("No such operation " + "log10" + ch + " in " + expression);
            }
            return IS_CHECKED ? new CheckedLog(parseStart()) : new Log(parseStart());
        }
        throw new NoSuchElementException("No argument in " + expression);
    }

    private PartOfExpression parseExpression() {
        skipWhitespaces();
        PartOfExpression part = parseSetClear();
        skipWhitespaces();
        return part;
    }

    private PartOfExpression parseSetClear() {
        skipWhitespaces();
        PartOfExpression part = parsePlusMinus();
        skipWhitespaces();
        while (true) {
            if (test('s')) {
                expect("set");
                if (!Character.isWhitespace(ch) && ch != '(' && ch != '-') {
                    throw new UnsupportedOperation("No such operation " + "set" + ch + " in " + expression);
                }
                part = parseOperation("set", part, parsePlusMinus());
            } else if (test('c') ) {
                expect("clear");
                if (!Character.isWhitespace(ch) && ch != '(' && ch != '-') {
                    throw new UnsupportedOperation("No such operation " + "clear" + ch + " in " + expression);
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
        skipWhitespaces();
        PartOfExpression part = parseMulDiv();
       // System.out.println(ch);
        if (isUnsupportedCharacter() && !eof()) {
            throw new UnsupportedCharacter("Unsupported character " + ch + " in " + expression);
        }
        while (test('+') || test('-')) { // z + y - -30 + (z + x)
            part = parseOperation(String.valueOf(take()), part, parseMulDiv());
        }
        return part;
    }

    private PartOfExpression parseMulDiv() {
        skipWhitespaces();
        PartOfExpression part = parseStart();
        skipWhitespaces();
        if (isUnsupportedCharacter() && !eof()) {
            throw new UnsupportedCharacter("Unsupported character " + ch + " in " + expression);
        }
        while (test('*') || test('/')) {
            part = parseOperation(String.valueOf(take()), part, parseStart());
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
            throw new NumberFormatException("Spaces in number");
        }
        return new Const(value);
    }

    private static BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+" -> IS_CHECKED ? new CheckedAdd(left, right) : new Add(left, right);
            case "-" -> IS_CHECKED ? new CheckedSubtract(left, right) : new Subtract(left, right);
            case "*" -> IS_CHECKED ? new CheckedMultiply(left, right) : new Multiply(left, right);
            case "/" -> IS_CHECKED ? new CheckedDivide(left, right) : new Divide(left, right);
            case "set" -> new Set(left, right);
            case "clear" -> new Clear(left, right);
            default -> throw new UnsupportedOperation("Unsupported operation operation " + operation);
        };
    }
}



