import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.Stack;

public class Reverse {

    public static void main(String[] args) throws IOException { // O(n * m) n - amount of lines, m - average length of each line
        BufScanner consoleScanner = new BufScanner(System.in);
        Stack<Stack<Integer>> lines = new Stack<>();
        String line = consoleScanner.nextLine();
        while (line != null) {
            //Scanner stringScanner = new Scanner(line);
            BufScanner stringScanner = new BufScanner(line);
            Stack<Integer> values = new Stack<>();
            String number = stringScanner.next(true);
            while (number != null) {
                values.push(Integer.parseInt(number));
                number = stringScanner.next(true);
            }
            /*while (stringScanner.hasNextInt()) {
                values.push(stringScanner.nextInt());
            }*/
            lines.push(values);
            stringScanner.close();
            line = consoleScanner.nextLine();
        }
        consoleScanner.close();
        while (!lines.empty()) {
            Stack<Integer> l = lines.pop();
            while (!l.empty()) {
                System.out.print(l.pop());
                if (!l.empty()) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}