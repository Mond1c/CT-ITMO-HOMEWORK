package expression;

public class Divide extends BinaryOperation {
    public Divide(PartOfExpression left, PartOfExpression right) {
        super(left, right, Operation.DIVIDE);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) / right.evaluate(value);
    }
}
