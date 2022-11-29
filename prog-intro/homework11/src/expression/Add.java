package expression;

public class Add extends BinaryOperation {
    public Add(PartOfExpression left, PartOfExpression right) {
        super(left, right, Operation.ADD);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) + right.evaluate(value);
    }
}
