import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(expr(new Scanner(System.in).nextLine()));
    }

    public static int getPriority(char operation) {
        return switch (operation) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    public static boolean isOperation(char operation) {
        return operation == '+' || operation == '-' || operation == '*' || operation == '/';
    }

    public static long calculate(long x, long y, char operation) {
        return switch (operation) {
            case '+' -> x + y;
            case '-' -> x - y;
            case '/' -> x / y;
            case '*' -> x * y;
            default -> 0;
        };
    }

    public static boolean extract(final List<Long> values, final List<Character> operations) {
        if (values.size() < 2 || operations.isEmpty() || !isOperation(operations.get(operations.size() - 1))) {
            return false;
        }
        long y = values.remove(values.size() - 1);
        long x = values.remove(values.size() - 1);
        char operation = operations.remove(operations.size() - 1);
        values.add(calculate(x, y, operation));
        return true;
    }

    public static String expr(final String expression) {
        List<Long> values = new ArrayList<>();
        List<Character> operations = new ArrayList<>();
        int bracketCount = 0;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') continue;
            if (ch == '(') {
                operations.add('(');
                bracketCount++;
            } else if (ch == ')') {
                if (bracketCount <= 0) {
                    return "WRONG";
                }
                bracketCount--;
                while (!operations.isEmpty() && operations.get(operations.size() - 1) != '(') {
                    if (!extract(values, operations)) {
                        return "WRONG";
                    }
                }
                if (!operations.isEmpty()) {
                    operations.remove(operations.size() - 1);
                }
            } else if (isOperation(ch)) {
                while (!operations.isEmpty() && getPriority(operations.get(operations.size() - 1)) >= getPriority(ch)) {
                    if (!extract(values, operations)) {
                        return "WRONG";
                    }
                }
                operations.add(ch);
            } else if (Character.isDigit(ch)) {
                long value = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    value = (value * 10) + (expression.charAt(i++) - '0');
                }
                values.add(value);
                i--;
            } else {
                return "WRONG";
            }
        }
        while (!operations.isEmpty()) {
            if (!extract(values, operations)) {
                return "WRONG";
            }
        }
        if (values.size() != 1 || bracketCount != 0) {
            return "WRONG";
        }
        return String.valueOf(values.get(values.size() - 1));
    }
}
