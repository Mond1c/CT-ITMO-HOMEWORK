package expresssion;

public class Add extends BinaryOperation {
    public Add(Expression left, Expression right) {
        super(left, right, Operation.ADD);
    }

    @Override
    public int evaluate(int value) {
        return left.evaluate(value) + right.evaluate(value);
    }
}
