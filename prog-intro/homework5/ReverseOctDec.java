import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;;

public class ReverseOctDec {

    public static void main(String[] args)  { 
        // :NOTE: memory
        List<IntList> lines = new ArrayList<>();
        try (MyScanner consoleScanner = new MyScanner(System.in)) {
            while (consoleScanner.hasNextLine()) {
                String line = consoleScanner.nextLine();
                try (MyScanner stringScanner = new MyScanner(line)) {
                    IntList values = new IntList();
                    while (stringScanner.hasNextInt()) {
                        values.add(stringScanner.nextInt());
                    }
                    lines.add(values);
                } catch (IOException e) {
                    System.err.println("Problem with I/O operations.");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File doesn't exist.");
        } catch (IOException e) {
            System.err.println("Problem with I/O operations.");
        }
        for (int i = lines.size() - 1; i >= 0; i--) {
            for (int j = lines.get(i).size() - 1; j >= 0; j--) {
                System.out.print(lines.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
