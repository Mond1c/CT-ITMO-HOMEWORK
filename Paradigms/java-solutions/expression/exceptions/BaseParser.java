package expression.exceptions;

import java.util.function.Predicate;

public class BaseParser {
    private static final char END = 0;

    final protected CharSource source;
    private char ch;

    public BaseParser(CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        } else {
            return false;
        }
    }

    protected boolean take(final String expected) {
        for (char ch: expected.toCharArray()) {
            if (!take(ch)) {
                return false;
            }
        }
        return true;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        return test(expected.charAt(0)) && source.test(expected.substring(1));
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error(String.format("Expected '%s', found '%s'", expected, ch != END ? ch : "EOI"));
        }
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected int getPos() { return source.getPos(); }

    protected void expect(final String expected) {
        for (int i = 0; i < expected.length(); i++) {
            char curCh = expected.charAt(i);
            if (!take(curCh)) {
                if (i == 0) {
                    throw error(String.format("Expected '%s', found '%s'", expected, ch != END ? ch : "EOI"));
                } else {
                    throw error(String.format("Expected '%s', found '%s'", expected, expected.substring(0, i - 1) + curCh));
                }
            }
        }
    }

    protected boolean test(Predicate<Character> pred) {
        return pred.test(ch);
    }

    protected boolean end() {
        return test(END);
    }

    protected boolean between(final char min, final char max) {
        return min <= ch && ch <= max;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }
}
