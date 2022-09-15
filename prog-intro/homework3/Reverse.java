import java.util.Scanner;
import java.util.Stack;

public class Reverse {
    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);
        Stack<Stack<Integer>> lines = new Stack<>();
        while (consoleScanner.hasNextLine()) {
            Scanner stringScanner = new Scanner(consoleScanner.nextLine());
            Stack<Integer> values = new Stack<>();
            while (stringScanner.hasNextInt()) {
                values.push(stringScanner.nextInt());
            }
            lines.push(values);
        }
        while (!lines.empty()) {
            Stack<Integer> line = lines.pop();
            while (!line.empty()) {
                System.out.print(line.pop());
                if (!line.empty()) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}