import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;;

public class ReverseOctDec {

    public static void main(String[] args)  { 
        List<List<Integer>> lines = new ArrayList<>();
        try (MyScanner consoleScanner = new MyScanner(System.in)) {
            String line = consoleScanner.nextLine();
            while (line != null) {
                try (MyScanner stringScanner = new MyScanner(line, true)) {
                    List<Integer> values = new ArrayList<>();
                    String number = stringScanner.next();
                    while (number != null) {
                        if (Character.toLowerCase(number.charAt(number.length() - 1)) == 'o') {
                            values.add(Integer.parseUnsignedInt(number.substring(0, number.length() - 1), 8));
                        } else {
                            values.add(Integer.parseInt(number));
                        }
                        number = stringScanner.next();
                    }
                    lines.add(values);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                line = consoleScanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = lines.size() - 1; i >= 0; i--) {
            for (int j = lines.get(i).size() - 1; j >= 0; j--) {
                System.out.print(lines.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
