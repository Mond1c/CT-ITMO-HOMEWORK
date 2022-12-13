package expression.parser;

public class Main {
    public static void main(String[] args) {
        ExpressionParser parser = new ExpressionParser();
        System.out.println(parser.parse("(0 + 0)").toMiniString());
        // z + y - -30 + z + x
    }
}
/*
Exp: (-(-((x + -2147483648))) + (x * (x * -(100))))
Exp: - -(x + -2147483648) + x * x * - 100
Exp: - -(x + -2147483648) + x * (x * - 100)
 */