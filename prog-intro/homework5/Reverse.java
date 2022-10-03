import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.Stack;

public class Reverse {

    public static void main(String[] args) throws IOException { // O(n * m) n - amount of lines, m - average length of each line
        Scanner consoleScanner = new Scanner(new InputStreamReader(System.in));
        Stack<Stack<Integer>> lines = new Stack<>();
        String line = consoleScanner.nextLine();
        while (line != null) {
           // System.err.println(consoleScanner.hasNextLine());
           // System.err.println(line);
            BufScanner stringScanner = new BufScanner(new StringReader(line));
            Stack<Integer> values = new Stack<>();
            while (stringScanner.hasNext(true)) {
                values.push(Integer.parseInt(stringScanner.next(true)));
            }
            lines.push(values);
            stringScanner.close();
            line = consoleScanner.nextLine();
        }
        consoleScanner.close();
        while (!lines.empty()) {
            Stack<Integer> l = lines.pop();
            while (!l.empty()) {
                int value = l.pop();
              //  System.err.print(value);
                System.out.print(value);
                if (!l.empty()) {
                   // System.err.print(" ");
                    System.out.print(" ");
                }
            }
           // System.err.println();
            System.out.println();
        }
    }
}