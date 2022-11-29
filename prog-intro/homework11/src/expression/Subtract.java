package expression;

public class Subtract extends  BinaryOperation {
    public Subtract(PartOfExpression left, PartOfExpression right) {
        super(left, right, Operation.SUBTRACT);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) - right.evaluate(value);
    }
}
