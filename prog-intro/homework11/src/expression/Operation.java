package expression;

// :NOTE: new ops
public enum Operation {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    NONE("");

    private final String operation;

    Operation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return operation;
    }
}
