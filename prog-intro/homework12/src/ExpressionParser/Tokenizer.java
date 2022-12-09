package ExpressionParser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private final String operators;
    private final List<String> tokens;
    private int pointer = 0;

    public Tokenizer(final String source, final String operators) {
        this.source = source;
        this.operators = operators;
        this.tokens = new ArrayList<>();
        tokenize();
    }

    public boolean hasNextToken() {
        return pointer < tokens.size();
    }

    public String nextToken() {
        if (!hasNextToken()) {
            throw new IndexOutOfBoundsException("Next token does not exist");
        }
        return tokens.get(pointer++);
    }

    private void tokenize() {
        final StringBuilder builder = new StringBuilder();
        for (char character : source.toCharArray()) {
            if (operators.indexOf(character) != -1) {
                if (!builder.isEmpty()) {
                    tokens.add(builder.toString());
                    builder.setLength(0);
                }
                tokens.add(String.valueOf(character));
            } else if (!Character.isWhitespace(character)) {
                builder.append(character);
            }
        }
    }
}
