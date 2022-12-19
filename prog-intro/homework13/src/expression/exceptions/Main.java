package expression.exceptions;

import expression.PartOfExpression;
import expression.TripleExpression;
import expression.parser.ExpressionParser;

public class Main {
    private final static ExpressionParser PARSER = new ExpressionParser();

    private static void run(final TripleExpression expr, int x) {
        try {
            final int result = expr.evaluate(x, 0, 0);
            System.out.println(x + "\t" + result);
        } catch (RuntimeException e) {
            System.out.println(x + "\t" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        final TripleExpression expr = PARSER.parse("1000000*x*x*x*x*x/(x-1)");
        System.out.println("x\tf");
        for (int x = 0; x <= 10; x++) {
            run(expr, x);
        }
    }
}
