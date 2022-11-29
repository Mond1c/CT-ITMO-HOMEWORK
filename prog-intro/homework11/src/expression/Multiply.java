package expression;

public class Multiply extends BinaryOperation {
    public Multiply(PartOfExpression left, PartOfExpression right) {
        super(left, right, Operation.MULTIPLY);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) * right.evaluate(value);
    }
}
