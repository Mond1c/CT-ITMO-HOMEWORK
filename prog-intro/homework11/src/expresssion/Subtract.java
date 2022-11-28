package expresssion;

public class Subtract extends  BinaryOperation {
    public Subtract(Expression left, Expression right) {
        super(left, right, Operation.SUBTRACT);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) - right.evaluate(value);
    }
}
