import java.io.*;

public class Main {
  public static void main(String[] args) {
      try (MyScanner scanner = new MyScanner("123 123")) {
        int a = scanner.nextInt(); int b = scanner.nextInt();
        System.out.println(a + " " + b);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}