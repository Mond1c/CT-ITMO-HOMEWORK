import java.util.Scanner;

public class Nine {
    private static void solve(int n, int open, int close, String str) {
        if (open == n && close == n) {
            System.out.println(str);
            return;
        }
        if (open < n) {
            solve(n, open + 1, close, str + "(");
        }
        if (close < open) {
            solve(n, open, close + 1, str + ")");
        }
    }

    public static void main(String[] args) {
        final int n = new Scanner(System.in).nextInt();
        solve(n, 0, 0, "");
    }
}