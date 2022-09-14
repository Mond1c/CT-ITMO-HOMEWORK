import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;

public class Reverse {
    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);
        List<String> lines = new LinkedList<>();
        while (consoleScanner.hasNextLine()) {
            Scanner stringScanner = new Scanner(consoleScanner.nextLine());
            StringBuilder stringBuilder = new StringBuilder();
            List<Integer> values = new LinkedList<>();
            while (stringScanner.hasNextInt()) {
                values.add(stringScanner.nextInt());
            }
            for (int i = values.size() - 1; i >= 0; i--) {
                stringBuilder.append(values.get(i));
                if (i != 0) {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("\n");
            lines.add(stringBuilder.toString());
        }
        for (int i = lines.size() - 1; i >= 0; i--) {
            System.out.print(lines.get(i));
        }
    }
}