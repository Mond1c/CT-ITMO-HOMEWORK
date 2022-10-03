import java.util.Scanner;
import java.util.Stack;

public class Reverse {
    public static void main(String[] args) { // O(n * m) n - amount of lines, m - average length of each line
        Scanner consoleScanner = new Scanner(System.in);
        Stack<Stack<Integer>> lines = new Stack<>();
        while (consoleScanner.hasNextLine()) {
            String line = consoleScanner.nextLine();
            Scanner stringScanner = new Scanner(line);
            System.err.println(line);
            Stack<Integer> values = new Stack<>();
            while (stringScanner.hasNextInt()) {
                values.push(stringScanner.nextInt());
            }
            lines.push(values);
            stringScanner.close();
        }
        consoleScanner.close();
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
