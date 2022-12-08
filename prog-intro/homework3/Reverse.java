import java.util.Scanner;
import java.util.Stack;
import java.io.IOException;

public class Reverse {
    public static void main(String[] args) { // O(n * m) n - amount of lines, m - average length of each line
        try (MyScanner consoleScanner = new MyScanner(System.in)) {
            Stack<Stack<Integer>> lines = new Stack<>();
            int i = 1;
            while (consoleScanner.hasNextLine()) {
                String line = consoleScanner.nextLine();
                MyScanner stringScanner = new MyScanner(line);
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
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
