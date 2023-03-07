package expression.exceptions;

import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        String exprString = "1000000*x*x*x*x*x/(x-1)";
        try  {
            TripleExpression expr = new ExpressionParser().parse(exprString);
            System.out.println("x\tf");
            for (int x = 0; x <= 10; ++x) {
                String result;
                try {
                    result = String.valueOf(expr.evaluate(x, 0, 0));
                } catch (ExpressionException e) {
                    result = e.getMessage();
                } 
                System.out.format("%d\t%s\n", x, result);
            }
        } catch (ParserException e) {
            System.err.println(e);
        }
    }
}