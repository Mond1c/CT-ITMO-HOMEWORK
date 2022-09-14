import java.util.Scanner;
import java.util.Stack;

public class Reverse {
    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);
        Stack<String> lines = new Stack<>();
        while (consoleScanner.hasNextLine()) {
            Scanner stringScanner = new Scanner(consoleScanner.nextLine());
            StringBuilder stringBuilder = new StringBuilder();
            Stack<Integer> values = new Stack<>();
            while (stringScanner.hasNextInt()) {
                values.push(stringScanner.nextInt());
            }
            while (!values.empty()) {
                stringBuilder.append(values.pop());
                if (!values.empty()) {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("\n");
            lines.push(stringBuilder.toString());
        }
        while (!lines.empty()) {
            System.out.print(lines.pop());
        }
    }
}