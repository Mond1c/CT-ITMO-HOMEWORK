package expression.parser;

import expression.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser extends BaseParser implements TripleParser {

    private static final String OPERATIONS = "+-*/";
    private boolean unaryMinus = false;
    private boolean minus = false;
    private int brackets = 0;
    private List<PartOfExpression> values;
    private List<String> operations;


    @Override
    public TripleExpression parse(final String expression) {
        System.out.println(expression);
        values = new ArrayList<>();
        operations = new ArrayList<>();
        List<Integer> openBracketStarts = new ArrayList<>();
        brackets = 0;
        setSource(new StringSource(expression));
        while (!eof()) {
            skipWhitespaces();
            if (take('(')) {
                brackets++;
                openBracketStarts.add(operations.size());
                if (minus) {
                    unaryMinus = true;
                    minus = false;
                }
                continue;
            } else if (take(')')) {
                final int op = openBracketStarts.remove(openBracketStarts.size() - 1);
                while (operations.size() != op) {
                    makeOperation();
                }
                brackets--;
            } else if (take('-')) {
                if (between('0', '9')) {
                    minus = true;
                    unaryMinus = false;
                    values.add(parseConst());
                } else {
                    operations.add("-");
                }
            } else if (between('0', '9')) {
                values.add(parseConst());
            } else if (isVariable(ch)) {
                values.add(parseVariable(ch));
                take();
            } else if (OPERATIONS.indexOf(ch) != -1) {
                operations.add(String.valueOf(ch));
                take();
                unaryMinus = false;
                minus = false;
                continue;
            }
            unaryMinus = false;
            minus = false;
        }
        while (!operations.isEmpty()) {
            makeOperation();
        }
        return values.get(0);
    }


    private void makeOperation() {
        final PartOfExpression right = values.remove(values.size() - 1);
        final PartOfExpression left = values.remove(values.size() - 1);
        final BinaryOperation operation = parseOperation(
                operations.remove(operations.size() - 1), left, right);
        values.add(operation);
    }

    private static boolean isVariable(char ch) {
        return ch == 'x' || ch == 'y' || ch == 'z';
    }

    private PartOfExpression parseConst() {
        final StringBuilder builder = new StringBuilder();
        if (minus) {
            builder.append('-');
        }
        boolean isDouble = false;
        while (between('0', '9')|| ch == '.') {
            if (ch == '.') {
                isDouble = true;
            }
            builder.append(ch);
            take();
        }
        final Const value;
        if (isDouble) {
            value = new Const(Double.parseDouble(builder.toString()));
        } else {
            value = new Const(Integer.parseInt(builder.toString()));
        }
        if (unaryMinus) {
            unaryMinus = false;
            return new Minus(value);
        }
        return value;
    }

    private static PartOfExpression parseVariable(char name) {
        final Variable variable;
        switch (name) {
            case 'x' -> variable = new Variable("x");
            case 'y' -> variable = new Variable("y");
            case 'z' -> variable = new Variable("z");
            default -> throw new AssertionError("Invalid variable name!");
        }
        return variable;
    }

    private static BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+" -> new Add(left, right);
            case "-" -> new Subtract(left, right);
            case "*" -> new Multiply(left, right);
            case "/" -> new Divide(left, right);
            default -> throw new AssertionError("Invalid operation!");
        };
    }
}




