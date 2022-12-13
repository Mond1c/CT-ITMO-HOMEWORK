package expression.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tokenizer {
    private final String source;
    private final List<String> operators;
    private final List<Character> variablesName;
    private final List<String> tokens;
    private final Set<Character> allCharactersForOperations;
    private int pointer = 0;

    public Tokenizer(final String source, final List<String> operators, List<Character> variablesName) {
        this.source = source;
        this.operators = operators;
        this.variablesName = variablesName;
        this.tokens = new ArrayList<>();
        this.allCharactersForOperations = new HashSet<>();
        for (final String operator : operators) {
            for (char character : operator.toCharArray()) {
                allCharactersForOperations.add(character);
            }
        }
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

    private void tokenize() { // TODO: make it faster with substring I think
        StringBuilder value = new StringBuilder();
        final StringBuilder operation = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char character = source.charAt(i);
            if (!Character.isWhitespace(character)) {
                if (Character.isDigit(character) || variablesName.contains(character)
                    || (tokens.isEmpty() || !tokens.get(tokens.size() - 1).equals(")"))
                    && value.isEmpty() && character == '-' && i + 1 < source.length() && (Character.isDigit(source.charAt(i + 1)) || variablesName.contains(source.charAt(i + 1)))) {
                    value.append(character);
                } else if (allCharactersForOperations.contains(character)) {
                    operation.append(character);
                }
            }
            if (operators.contains(operation.toString())) {
                if (!value.isEmpty()) {
                    tokens.add(value.toString());
                    value.setLength(0);
                }
                tokens.add(operation.toString());
                operation.setLength(0);
            }
        }
        if (!value.isEmpty()) {
            tokens.add(value.toString());
        }
    //    System.out.println(tokens.toString());
    }
}
