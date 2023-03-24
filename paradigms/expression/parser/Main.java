package expression.parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception  {
        System.out.println(new ExpressionParser().parse("2 * (3 + 2)").evaluate(0)); // Answer: 709955719
    }
}
