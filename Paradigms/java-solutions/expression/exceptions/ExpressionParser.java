package expression.exceptions;

import expression.MegaExpression;
import expression.SetBit;

import java.util.List;
import java.util.Optional;

import expression.ClearBit;
import expression.CountBits;
import expression.Const;
import expression.Variable;

public class ExpressionParser implements TripleParser {
    public ExpressionParser() {}

    @Override
    public MegaExpression parse(String expression) throws ParserException {
        return new ExprParser(new StringCharSource(expression)).parse();
    }

    private static class ExprParser extends BaseParser {

        private static List<String> binaryOperators = List.of(
            "count",
            "set",
            "clear"
        );

        //:Note: Final const
        private static List<String> possibleVariableNames = Variable.possibleVariableNames;

        public ExprParser(final CharSource source) {
            super(source);
        }

        public MegaExpression parse() throws ParserException {
            Optional<MegaExpression> expr = parseHead();
            if (!this.end()) {
                int pos = getPos();
                throw new ParserException("END", String.valueOf(take()), pos);
            } else if (expr.isEmpty()) {
                throw new ParserException("Input is empty"); 
            } else {
                return expr.get();
            }
        }

        private Optional<MegaExpression> parseHead() throws ParserException {
            return parseSetClear();
        }

        private Optional<MegaExpression> parseUnary() throws ParserException {
            skipWhitespace();
            int pos = getPos();
            if (test(Character::isDigit)) {
                StringBuilder number = new StringBuilder();
                while (test(Character::isDigit)) {
                    number.append(take());
                }
                try {
                    return Optional.of(new Const(Integer.parseInt(number.toString())));
                } catch (NumberFormatException e) {
                    throw new ParserException("Parseable integer (from " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE + ")", number.toString(), pos);
                }
            } else if (take('-')) {
                if (test(Character::isDigit) && !(test('0'))) {
                    StringBuilder number = new StringBuilder("-");
                    while (test(Character::isDigit)) {
                        number.append(take());
                    }
                    try {
                        return Optional.of(new Const(Integer.parseInt(number.toString())));
                    } catch (NumberFormatException e) {
                        throw new ParserException("Parseable integer (from " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE + ")", number.toString(), pos);
                    }
                } else {
                    skipWhitespace();
                    Optional<MegaExpression> expr = parseUnary();
                    if (expr.isPresent()) {
                        return Optional.of(new CheckedNegate(expr.get()));
                    } else {
                        throw new ParserException("Expected argument for unary minus", pos);
                    }
                }
            } else if (take('(')) {
                Optional<MegaExpression> expr = parseHead();
                skipWhitespace();
                pos = getPos();
                if (!take(')')) {
                    throw new ParserException(")", String.valueOf(take()), pos);
                } else if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for ()", pos);
                } else {
                    return expr;
                }
            } else if (test("count")) { 
                take("count");
                checkForNoLetter("count", pos);
                Optional<MegaExpression> expr = parseUnary();
                if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for count", pos);
                } else {
                    return Optional.of(new CountBits(expr.get()));
                }
            } else if (test("log10")) {
                take("log10");
                checkForNoLetter("log10", pos);
                Optional<MegaExpression> expr = parseUnary();
                if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for log10", pos); 
                } else {
                    return Optional.of(new CheckedLog(expr.get()));
                }
            } else if (test("pow10")) {
                take("pow10");
                checkForNoLetter("pow10", pos);
                Optional<MegaExpression> expr = parseUnary();
                if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for pow10", pos); 
                } else {
                    return Optional.of(new CheckedPow(expr.get()));
                }
            } else {
                return parseVariable();
            }
        }
        
        private Optional<MegaExpression> parseVariable() throws ParserException {
            int pos = getPos();
            StringBuilder token = new StringBuilder();

            for (String s: possibleVariableNames) {
                if (test(s)) {
                    take(s);
                    return Optional.of(new Variable(s));
                }
            }


            // Something goes wrong.

            for (String s: binaryOperators) {
                if (test(s)) {
                    return Optional.empty();
                }
            }

            // Variable can be named with letters, digits, or connectors.
            while (test(ch -> Character.getType(ch) == Character.CONNECTOR_PUNCTUATION ||
                    Character.isLetterOrDigit(ch))) {
                token.append(take());
            }

            if (token.isEmpty()) {
                return Optional.empty();
            } else {
                throw new ParserException("Unknown token '" + token.toString() + "'", pos);
            }
        }

        private Optional<MegaExpression> parseMultiplyDivide() throws ParserException {
            Optional<MegaExpression> left = parseUnary();
            while (true) {
                skipWhitespace();
                int pos = getPos();
                if (take('*')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for *", pos);
                    }
                    Optional<MegaExpression> right = parseUnary();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for *", pos);
                    } else {
                        left = Optional.of(new CheckedMultiply(left.get(), right.get()));
                    }
                } else if (take('/')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for /", pos);
                    }
                    Optional<MegaExpression> right = parseUnary();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for /", pos);
                    } else {
                        left = Optional.of(new CheckedDivide(left.get(), right.get()));
                    }
                } else {
                    return left;
                }
            }
        }

        private Optional<MegaExpression> parseAddSubtract() throws ParserException {
            Optional<MegaExpression> left = parseMultiplyDivide();
            while (true) {
                skipWhitespace();
                int pos = getPos();
                if (take('+')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for +", pos);
                    }
                    Optional<MegaExpression> right = parseMultiplyDivide();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for +", pos);
                    } else {
                        left = Optional.of(new CheckedAdd(left.get(), right.get()));
                    }
                } else if (take('-')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for -", pos);
                    }
                    Optional<MegaExpression> right = parseMultiplyDivide();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for -", pos);
                    } else {
                        left = Optional.of(new CheckedSubtract(left.get(), right.get()));
                    }
                } else { 
                    return left;
                }
            }
        }

        private Optional<MegaExpression> parseSetClear() throws ParserException {
            Optional<MegaExpression> left = parseAddSubtract();
            while (true) {
                skipWhitespace();
                int pos = getPos();
                if (test("set")) {
                    take("set");
                    checkForNoLetter("set", pos);
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for set", pos);
                    }
                    Optional<MegaExpression> right = parseAddSubtract();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for set", pos);
                    } else {
                        left = Optional.of(new SetBit(left.get(), right.get()));
                    }
                } else if (test("clear")) {
                    take("clear");
                    checkForNoLetter("clear", pos);
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for clear", pos);
                    }
                    Optional<MegaExpression> right = parseAddSubtract();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for clear", pos);
                    } else {
                        left = Optional.of(new ClearBit(left.get(), right.get()));
                    }
                } else {
                    return left;
                }
            }
        }

        private void checkForNoLetter(String token, int pos) throws ParserException {
            if (!waitNotLetter()) {
                throw new ParserException(token, token + take(), pos);
            }
        }

        private boolean waitNotLetter() {
            return (test('(') || test(')') || test('+') || test('-') || test(Character::isWhitespace) || end());
        }
    }
}