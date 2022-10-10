import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Reverse {

    public static void main(String[] args)  { 
        List<List<Integer>> lines = new ArrayList<>();
        try (MyScanner consoleScanner = new MyScanner(System.in)) {
            while (consoleScanner.hasNextLine()) {
                String line = consoleScanner.nextLine();
                try (MyScanner stringScanner = new MyScanner(line)) {
                    List<Integer> values = new ArrayList<>();
                    while (stringScanner.hasNextInt()) {
                        values.add(stringScanner.nextInt());
                    }
                    lines.add(values);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
