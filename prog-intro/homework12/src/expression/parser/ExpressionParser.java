package expression.parser;

import expression.*;

import java.util.*;

public class ExpressionParser extends BaseParser implements TripleParser {

    private static class Pair {
        public final String first;
        public final String second;

        public Pair(final String first, final String second) {
            this.first = first;
            this.second = second;
        }
    }

    private static int counter = 0;
    private static final Map<String, Integer> OPERATIONS
            = Map.of("+", 0, "-", 0, "*", 1, "/", 1, "(", 0, ")", 0);
    private Stack<Pair> operations;
    private Stack<PartOfExpression> values;

    private boolean isUnaryMinus = false;
    private int bracketCounter = 0;

    @Override
    public TripleExpression parse(final String expression) {
        setNewSource(new StringSource(expression));
        return parseSubexpression();
    }

    private PartOfExpression parseSubexpression() {
        List<PartOfExpression> parts = new ArrayList<>();
        String lastOperation = "";
        while (!eof()) {
            skipExtraSymbols();
            System.out.println(ch);
            if (take('(')) {
                parts.add(parseSubexpression());
                bracketCounter++;
            } else if (take(')')) {
                //System.out.println(123);
                bracketCounter--;
                take();
                return parts.get(parts.size() - 1);
            } else if (take('-')) {
                if (lastOperation.equals("") && parts.isEmpty()) {
                    isUnaryMinus = true;
                } else {
                    lastOperation = getOperation();
                }
                take();
            } else if (isOperation()) {
                lastOperation = getOperation();
                take();
            } else if (Character.isLetter(ch)) {
                final PartOfExpression variable = getVariable();
                parts.add(variable);
                take();
            } else {
                final PartOfExpression constant = getConst();
                parts.add(constant);
            }
            if (parts.size() > 1 && OPERATIONS.containsKey(lastOperation)) {
                final BinaryOperation operation
                        = parseOperation(lastOperation, parts.get(parts.size() - 2), parts.get(parts.size() - 1));
                parts.remove(parts.size() - 1);
                parts.remove(parts.size() - 1);
                parts.add(operation);
                lastOperation = "";
            }
        }
        return parts.get(parts.size() - 1);
    }

    private PartOfExpression getConst() {
        final StringBuilder builder = new StringBuilder();
        while (between('0', '9')) {
            builder.append(ch);
            take();
        }
        if (isUnaryMinus && bracketCounter > 0) {
            isUnaryMinus = false;
            return new Minus(parseValueToken(builder.toString()));
        }
        if (isUnaryMinus) {
            isUnaryMinus = false;
            return parseValueToken("-" + builder);
        }
        return parseValueToken(builder.toString());
    }

    private boolean isOperation() {
        return OPERATIONS.containsKey(getOperation());
    }

    private String getOperation() {
        return String.valueOf(ch);
    }

    private PartOfExpression getVariable() {
        return parseValueToken(String.valueOf(ch));
    }


/*
    public TripleExpression parse(final String expression) {
        counter++;
        System.out.println(counter);
        this.values = new Stack<>();
        this.operations = new Stack<>();
        final Tokenizer stringTokenizer = new Tokenizer(expression, List.of(" ", "+", "-", "*", "/", "(", ")"), List.of('x', 'y', 'z'));
        System.out.println("Exp: " + expression);
        String prevToken = "";
        while (stringTokenizer.hasNextToken()) {
            final String curToken = stringTokenizer.nextToken();
            if (OPERATIONS.containsKey(curToken)) {
                if (curToken.equals(")")) {
                    while (!operations.top().first.equals("(") && values.size() > 1) {
                        final PartOfExpression right = values.pop();
                        final PartOfExpression left = values.pop();
                        values.add(parseOperation(operations.pop().first, left, right));
                    }
                    boolean bracketDeleted = false;
                    if (operations.top().first.equals("(")) {
                        operations.pop();
                        bracketDeleted = true;
                    }
                    while (!operations.isEmpty()  && operations.top().first.equals("-")
                        && (values.size() == 1 || OPERATIONS.containsKey(operations.top().second))) {
                        values.push(new Minus(values.pop()));
                        operations.pop();
                    }
                    if (!bracketDeleted) {
                        operations.pop();
                    }
                    while (!operations.isEmpty() && (operations.top().first.equals("*") || operations.top().first.equals("/"))) {
                        final PartOfExpression right = values.pop();
                        final PartOfExpression left = values.pop();
                        values.add(parseOperation(operations.pop().first, left, right));
                    }
                    continue;
                }
                operations.add(new Pair(curToken, prevToken));
            } else {
                values.add(parseValueToken(curToken));
                while (!operations.isEmpty()  && operations.top().first.equals("-")
                        && (values.size() == 1 || OPERATIONS.containsKey(operations.top().second) && !operations.top().second.equals(")"))) {
                    values.push(new Minus(values.pop()));
                    operations.pop();
                }
                if (!operations.isEmpty() && (operations.top().first.equals("*") || operations.top().first.equals("/"))) {
                    final PartOfExpression right = values.pop();
                    final PartOfExpression left = values.pop();
                    values.add(parseOperation(operations.pop().first, left, right));
                }
            }
            prevToken = curToken;
        }
        while (!operations.isEmpty()  && operations.top().first.equals("-")
                && (values.size() == 1 || OPERATIONS.containsKey(operations.top().second) && !operations.top().second.equals(")"))) {
            values.push(new Minus(values.pop()));
            operations.pop();
        }
        PartOfExpression lastPart = null;
        int pos = 0;
        for (Pair pair : operations) {
            final String operation = pair.first;
            if (operations.equals("(")) {
                continue;
            }
            final PartOfExpression left;
            final PartOfExpression right;
            if (lastPart != null) {
                left = lastPart;
            } else {
                left = values.get(pos++);
            }
            right = values.get(pos++);
            lastPart = parseOperation(operation, left, right);
        }
        return lastPart != null ? lastPart : values.top();
    }
*/

    private PartOfExpression parseValueToken(final String token) {
        if (Character.isDigit(token.charAt(0)) || token.charAt(0) == '-' && Character.isDigit(token.charAt(1))) {
            if (token.charAt(0) == '-' && token.substring(1).equals("0")) {
                return new Minus(new Const(Integer.parseInt(token.substring(1))));
            }
            return new Const(Integer.parseInt(token));
        }
        if (token.charAt(0) == '-') {
            return new Minus(new Variable(token.substring(1)));
        }
        return new Variable(token);
    }

    private BinaryOperation parseOperation(final String operation, final PartOfExpression left, final PartOfExpression right) {
        return switch (operation) {
            case "+" -> new Add(left, right, bracketCounter * 2);
            case "-" -> new Subtract(left, right, bracketCounter * 2);
            case "*" -> new Multiply(left, right, bracketCounter * 2);
            case "/" -> new Divide(left, right, bracketCounter * 2);
            case "(" -> new OpenBracket();
            default -> throw new AssertionError("Unsupported operation!");
        };
    }
}

