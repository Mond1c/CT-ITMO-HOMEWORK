package expresssion;

public class Multiply extends BinaryOperation {
    public Multiply(Expression left, Expression right) {
        super(left, right, Operation.MULTIPLY);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) * right.evaluate(value);
    }
}
