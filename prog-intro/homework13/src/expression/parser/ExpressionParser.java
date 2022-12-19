package expression.parser;

import expression.*;
import expression.exceptions.*;

public class ExpressionParser extends BaseParser implements TripleParser {

    private String expression;

    @Override
    public TripleExpression parse(String expression) throws Exception {
        setSource(new StringSource(expression));
        this.expression = expression;
        return parseExpression();
    }

    private PartOfExpression parseStart()  throws Exception {
        skipWhitespaces();
        if (take('(')) {
            PartOfExpression part = parseExpression();
            expect(')');
            return part;
        } else if (between('0', '9')){
            return parseConst(false);
        } else if (between('x', 'z')) {
            return parseVariable();
        } else if (take('-')) {
            if (between('0', '9')) {
                return parseConst(true);
            }
            return new CheckedNegate(parseStart());
        } else if (take('c')) {
            expect("ount");
            return new Count(parseStart());
        }
        throw new AssertionError("Invalid character in " + expression + ": " + ch);
    }

    private PartOfExpression parseExpression() throws Exception {
        skipWhitespaces();
        PartOfExpression part = parseSetClear();
        skipWhitespaces();
        return part;
    }

    private PartOfExpression parseSetClear() throws Exception {
        skipWhitespaces();
        PartOfExpression part = parsePlusMinus();
        skipWhitespaces();
        while (true) {
            if (test('s')) {
                expect("set");
                part = parseOperation("set", part, parsePlusMinus());
            } else if (test('c') ) {
                expect("clear");
                part = parseOperation("clear", part, parsePlusMinus());
            } else {
                break;
            }
        }
        return part;
    }

    private PartOfExpression parsePlusMinus() throws Exception {
        skipWhitespaces();
        PartOfExpression part = parseMulDiv();
        skipWhitespaces();
        while (test('+') || test('-')) { // z + y - -30 + (z + x)
            part = parseOperation(String.valueOf(take()), part, parseMulDiv());
        }
        return part;
    }

    private PartOfExpression parseMulDiv() throws Exception {
        skipWhitespaces();
        PartOfExpression part = parseStart();
        skipWhitespaces();
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
        return new Const(value);
    }

    private static BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+" -> new CheckedAdd(left, right);
            case "-" -> new CheckedSubtract(left, right);
            case "*" -> new CheckedMultiply(left, right);
            case "/" -> new CheckedDivide(left, right);
            case "set" -> new Set(left, right);
            case "clear" -> new Clear(left, right);
            default -> throw new AssertionError("Invalid operation");
        };
    }
}



