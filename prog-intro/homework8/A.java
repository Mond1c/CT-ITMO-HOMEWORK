import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt(), b = scanner.nextInt(), n = scanner.nextInt();
        int diff1 = n - b, diff2 = b - a;
        int moves = diff1 / diff2;
        if (diff1 % diff2 != 0) {
            moves++;
        }
        System.out.println(moves * 2 + 1);
    }
}