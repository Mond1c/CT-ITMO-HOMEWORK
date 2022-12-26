package expression.parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception  {
        System.out.println(new ExpressionParser().parse("pow10x").toMiniString()); // Answer: 709955719
    }
}
