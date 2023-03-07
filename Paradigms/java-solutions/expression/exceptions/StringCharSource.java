package expression.exceptions;

public class StringCharSource implements CharSource {
    private final String string;
    private int pos;

    public StringCharSource(String string) {
        this.string = string;
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }

    @Override
    public int getPos() {
        return pos - 1;
    }

    @Override
    public boolean test(char ch) {
        return string.charAt(pos) == ch;
    }

    @Override
    public boolean test(String str) {
        return string.length() - pos >= str.length() && string.substring(pos, pos + str.length()).equals(str);
    }
}