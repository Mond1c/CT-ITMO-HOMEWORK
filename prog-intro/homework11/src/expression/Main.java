package expression;

public class Main {
    public static void main(String[] args) {
        Expression exp = new Subtract(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Const(3)
        );
        System.out.println(exp.evaluate(5));
        System.out.println(exp);
        System.out.println(exp.toMiniString());

        System.out.println(new Multiply(new Const(2), new Variable("x"))
                .equals(new Multiply(new Const(2), new Variable("x"))));

        System.out.println(new Multiply(new Const(2), new Variable("x"))
                .equals(new Multiply(new Variable("x"), new Const(2))));

        System.out.println(new Add(new Variable("x"), new Variable("x")).equals(new Add(new Variable("x"), new Variable("x"))));
    }
}