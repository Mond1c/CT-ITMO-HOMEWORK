import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.Stack;

public class Reverse {

    public static void main(String[] args)  { 
        Stack<Stack<Integer>> lines = new Stack<>();
        try (BufScanner consoleScanner = new BufScanner(System.in)) {
            String line = consoleScanner.nextLine();
            while (line != null) {
                try (MyScanner stringScanner = new MyScanner(line)) {
                    Stack<Integer> values = new Stack<>();
                    String number = stringScanner.next(true);
                    while (number != null) {
                        values.push(Integer.parseInt(number));
                        number = stringScanner.next(true);
                    }
                    lines.push(values);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                line = consoleScanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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