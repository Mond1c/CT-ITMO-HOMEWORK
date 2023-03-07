package expression.exceptions;

public interface CharSource {
    char next();
    boolean hasNext();

    boolean test(char str);
    boolean test(String str);

    int getPos();
    IllegalArgumentException error(String message);
}
