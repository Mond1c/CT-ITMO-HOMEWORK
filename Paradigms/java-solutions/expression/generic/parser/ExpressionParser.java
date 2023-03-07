package expression.generic.parser;

import java.util.List;
import java.util.Optional;

import expression.exceptions.BaseParser;
import expression.exceptions.CharSource;
import expression.exceptions.ParserException;
import expression.exceptions.StringCharSource;
import expression.generic.operations.Abs;
import expression.generic.operations.AbstractTripleExpression;
import expression.generic.operations.Add;
import expression.generic.operations.Const;
import expression.generic.operations.Divide;
import expression.generic.operations.Mod;
import expression.generic.operations.Negate;
import expression.generic.operations.Square;
import expression.generic.operations.Subtract;
import expression.generic.operations.TripleExpression;
import expression.generic.operations.Variable;
import expression.generic.operations.Multiply;

import expression.generic.types.NumberType;

public class ExpressionParser<T extends Comparable<T>> implements TripleParser<T> {
    public ExpressionParser() {}

    @Override
    public TripleExpression<T> parse(String expression, NumberType<T> performer) throws ParserException {
        return new ExprParser<>(new StringCharSource(expression), performer).parse();
    }

    private static class ExprParser<T extends Comparable<T>> extends BaseParser {

        private final NumberType<T> performer;

        private static final List<String> binaryOperators = List.of(
            "count",
            "set",
            "clear"
        );

        //:Note: Final const
        private static final List<String> possibleVariableNames = Variable.POSSIBLE_VARIABLE_NAMES;

        public ExprParser(final CharSource source, NumberType<T> performer) {
            super(source);
            this.performer = performer;
        }

        public AbstractTripleExpression<T> parse() throws ParserException {
            Optional<AbstractTripleExpression<T>> expr = parseHead();
            if (!this.end()) {
                int pos = getPos();
                throw new ParserException("END", String.valueOf(take()), pos);
            } else if (expr.isEmpty()) {
                throw new ParserException("Input is empty"); 
            } else {
                return expr.get();
            }
        }

        private Optional<AbstractTripleExpression<T>> parseHead() throws ParserException {
            return parseAddSubtract();
        }

        private Optional<AbstractTripleExpression<T>> parseUnary() throws ParserException {
            skipWhitespace();
            int pos = getPos();
            if (test(Character::isDigit)) {
                StringBuilder number = new StringBuilder();
                while (test(Character::isDigit)) {
                    number.append(take());
                }
                try {
                    return Optional.of(new Const<>(number.toString(), performer));
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
                        return Optional.of(new Const<>(number.toString(), performer));
                    } catch (NumberFormatException e) {
                        throw new ParserException("Parseable integer (from " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE + ")", number.toString(), pos);
                    }
                } else {
                    skipWhitespace();
                    Optional<AbstractTripleExpression<T>> expr = parseUnary();
                    if (expr.isPresent()) {
                        return Optional.of(new Negate<>(expr.get()));
                    } else {
                        throw new ParserException("Expected argument for unary minus", pos);
                    }
                }
            } else if (take('(')) {
                Optional<AbstractTripleExpression<T>> expr = parseHead();
                skipWhitespace();
                pos = getPos();
                if (!take(')')) {
                    throw new ParserException(")", String.valueOf(take()), pos);
                } else if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for ()", pos);
                } else {
                    return expr;
                }
            } else if (test("abs")) { 
                take("abs");
                checkForNoLetter("abs", pos);
                Optional<AbstractTripleExpression<T>> expr = parseUnary();
                if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for abs", pos);
                } else {
                    return Optional.of(new Abs<>(expr.get()));
                }
            } else if (test("square")) {
                take("square");
                checkForNoLetter("square", pos);
                Optional<AbstractTripleExpression<T>> expr = parseUnary();
                if (expr.isEmpty()) {
                    throw new ParserException("Expected argument for square", pos); 
                } else {
                    return Optional.of(new Square<>(expr.get()));
                }
            } else {
                return parseVariable();
            }
        }
        
        private Optional<AbstractTripleExpression<T>> parseVariable() throws ParserException {
            int pos = getPos();
            StringBuilder token = new StringBuilder();

            for (String s: possibleVariableNames) {
                if (test(s)) {
                    take(s);
                    return Optional.of(new Variable<>(s, performer));
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
                throw new ParserException("Unknown token '" + token + "'", pos);
            }
        }

        private Optional<AbstractTripleExpression<T>> parseMod() throws ParserException {
            Optional<AbstractTripleExpression<T>> left = parseUnary();
            while (true) {
                skipWhitespace();
                int pos = getPos();
                if (test("mod")) {
                    take("mod");
                    checkForNoLetter("mod", pos);
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for mod", pos);
                    }
                    Optional<AbstractTripleExpression<T>> right = parseAddSubtract();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for mod", pos);
                    } else {
                        left = Optional.of(new Mod<>(left.get(), right.get()));
                    }
                } else {
                    return left;
                }
            }
        }

        private Optional<AbstractTripleExpression<T>> parseMultiplyDivide() throws ParserException {
            Optional<AbstractTripleExpression<T>> left = parseMod();
            while (true) {
                skipWhitespace();
                int pos = getPos();
                if (take('*')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for *", pos);
                    }
                    Optional<AbstractTripleExpression<T>> right = parseMod();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for *", pos);
                    } else {
                        left = Optional.of(new Multiply<>(left.get(), right.get()));
                    }
                } else if (take('/')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for /", pos);
                    }
                    Optional<AbstractTripleExpression<T>> right = parseMod();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for /", pos);
                    } else {
                        left = Optional.of(new Divide<>(left.get(), right.get()));
                    }
                } else {
                    return left;
                }
            }
        }

        private Optional<AbstractTripleExpression<T>> parseAddSubtract() throws ParserException {
            Optional<AbstractTripleExpression<T>> left = parseMultiplyDivide();
            while (true) {
                skipWhitespace();
                int pos = getPos();
                if (take('+')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for +", pos);
                    }
                    Optional<AbstractTripleExpression<T>> right = parseMultiplyDivide();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for +", pos);
                    } else {
                        left = Optional.of(new Add<>(left.get(), right.get()));
                    }
                } else if (take('-')) {
                    if (left.isEmpty()) {
                        throw new ParserException("Expected left argument for -", pos);
                    }
                    Optional<AbstractTripleExpression<T>> right = parseMultiplyDivide();
                    if (right.isEmpty()) {
                        throw new ParserException("Expected right argument for -", pos);
                    } else {
                        left = Optional.of(new Subtract<>(left.get(), right.get()));
                    }
                } else { 
                    return left;
                }
            }
        }

        // private Optional<AbstractTripleExpression<T>> parseSetClear() throws ParserException {
        //     Optional<AbstractTripleExpression<T>> left = parseAddSubtract();
        //     while (true) {
        //         skipWhitespace();
        //         int pos = getPos();
        //         if (test("set")) {
        //             take("set");
        //             checkForNoLetter("set", pos);
        //             if (left.isEmpty()) {
        //                 throw new ParserException("Expected left argument for set", pos);
        //             }
        //             Optional<AbstractTripleExpression<T>> right = parseAddSubtract();
        //             if (right.isEmpty()) {
        //                 throw new ParserException("Expected right argument for set", pos);
        //             } else {
        //                 left = Optional.of(new SetBit(left.get(), right.get()));
        //             }
        //         } else if (test("clear")) {
        //             take("clear");
        //             checkForNoLetter("clear", pos);
        //             if (left.isEmpty()) {
        //                 throw new ParserException("Expected left argument for clear", pos);
        //             }
        //             Optional<AbstractTripleExpression<T>> right = parseAddSubtract();
        //             if (right.isEmpty()) {
        //                 throw new ParserException("Expected right argument for clear", pos);
        //             } else {
        //                 left = Optional.of(new ClearBit(left.get(), right.get()));
        //             }
        //         } else {
        //             return left;
        //         }
        //     }
        // }

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