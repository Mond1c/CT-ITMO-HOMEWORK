package expression.parser;

import expression.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExpressionParser extends BaseParser implements TripleParser {

    @Override
    public TripleExpression parse(String expression) {
        setSource(new StringSource(expression));
        return parseExpression();
    }

    private PartOfExpression parseStart()  {
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
            return new Minus(parseStart());
        } else if (take('c')) {
            expect("ount");
            return new Count(parseStart());
        }
        throw new AssertionError("Invalid character in expression");
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

    private PartOfExpression parsePlusMinus() {
        skipWhitespaces();
        PartOfExpression part = parseMulDiv();
        skipWhitespaces();
        while (test('+') || test('-')) { // z + y - -30 + (z + x)
            part = parseOperation(String.valueOf(take()), part, parseMulDiv());
        }
        return part;
    }

    private PartOfExpression parseMulDiv() {
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
        return new Const(Integer.parseInt(builder.toString()));
    }

    private static BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+" -> new Add(left, right);
            case "-" -> new Subtract(left, right);
            case "*" -> new Multiply(left, right);
            case "/" -> new Divide(left, right);
            case "set" -> new Set(left, right);
            case "clear" -> new Clear(left, right);
            default -> throw new AssertionError("Invalid operation");
        };
    }
}



