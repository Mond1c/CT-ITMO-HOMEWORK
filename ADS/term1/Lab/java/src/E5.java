import java.util.Arrays;
import java.util.Scanner;

public class E5 {
    public static int solve(final String str1, final String str2) {
        final int n = str1.length() + 1;
        final int m = str2.length() + 1;
        int[][] dp = new int[n][m];
        dp[0][0] = 0;
        for (int i = 1; i < n; i++) {
            dp[i][0] = i;
        }
        for (int j = 1; j < m; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                int a = dp[i][j - 1] + 1;
                int b = dp[i - 1][j] + 1;
                int c = dp[i - 1][j - 1];
                if (str1.charAt(i - 1) != str2.charAt(j - 1)) {
                    c++;
                }
                dp[i][j] = Math.min(a, Math.min(b, c));
            }
        }
        return dp[n - 1][m - 1];
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String str1 = scanner.next();
        final String str2 = scanner.next();
        System.out.println(solve(str1, str2));
    }
}
