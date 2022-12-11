package ExpressionParser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("2 + x * 2");
        System.out.println(parser.parse().evaluate(100));
    }
}
