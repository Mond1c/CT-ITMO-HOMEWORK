package ExpressionParser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser("x * (x - 2)*x + 1");
        parser.parse();
    }
}
