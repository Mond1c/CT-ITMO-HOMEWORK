import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        
        //System.out.println(new BufScanner(new StringReader("\n\n\n")).hasNextLine());
        try (MyScanner scanner = new MyScanner( new StringReader("123\njklsdg;d"))) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
