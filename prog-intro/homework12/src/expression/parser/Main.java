package expression.parser;

public class Main {
    public static void main(String[] args) {
        System.out.println(new ExpressionParser().parse("z + y - -30 + (z + x)").evaluate(-1803737379,
                -1558596764, -111338732)); // Answer: 709955719
    }
}
