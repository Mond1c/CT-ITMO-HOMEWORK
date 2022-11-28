package expresssion;

public interface Expression {
    int evaluate(final int value);
    String toMiniString();

    String getMiniString(Operation operation);
    boolean equals(final Expression other);
}
