package expression.parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException  {
        System.out.println(new ExpressionParser().parse("(x set (z set z))").toMiniString()); // Answer: 709955719
    }
}
