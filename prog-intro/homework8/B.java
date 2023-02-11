import java.util.Scanner;

public class B {
    private final static int MIN_N = 25000;
    private final static int MIN_MOD = -710;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int start = MIN_N * MIN_MOD;
        for (int i = 0; i < n; i++) {
            System.out.println(start);
            start -= MIN_MOD;
        }
    }
}
