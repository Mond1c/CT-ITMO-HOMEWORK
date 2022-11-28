package expresssion;

public class Divide extends BinaryOperation {
    public Divide(Expression left, Expression right) {
        super(left, right, Operation.DIVIDE);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) / right.evaluate(value);
    }
}
