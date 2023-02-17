import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class C8 {
    public static void generateAnswer(final List<Integer> ans, final int[][] dp, final int[] m, final int i, final int j) {
        if (dp[i][j] == 0) {
            return;
        } else if (dp[i - 1][j] == dp[i][j]) {
            generateAnswer(ans, dp, m, i - 1, j);
        } else {
            generateAnswer(ans, dp, m, i - 1, j - m[i - 1]);
            ans.add(i);
        }
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        final int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
        }
        final int s = scanner.nextInt();

        final int[][] dp = new int[n + 1][s + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= s; j++) {
                if (j < x[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - x[i - 1]] + x[i - 1]);
                }
            }
        }
        System.out.println(Arrays.deepToString(dp));
        List<Integer> ans = new ArrayList<>();
        generateAnswer(ans, dp, x, n, s);
        System.out.println(ans.size());
        for (int v : ans) {
            System.out.printf("%d ", v);
        }
        System.out.println();
    }
}
