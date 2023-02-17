import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class B8 {
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
        final int N = scanner.nextInt();
        final int M = scanner.nextInt();
        final int[] m = new int[N];
        final int[] c = new int[N];
        for (int i = 0; i < N; i++) {
            m[i] = scanner.nextInt();
        }
        for (int i = 0; i < N; i++) {
            c[i] = scanner.nextInt();
        }
        final int[][] dp = new int[N + 1][M + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                if (j >= m[i - 1]) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - m[i - 1]] + c[i - 1]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        List<Integer> ans = new ArrayList<>();
        generateAnswer(ans, dp, m, N, M);
        System.out.println(ans.size());
        for (int v : ans) {
            System.out.printf("%d ", v);
        }
        System.out.println();
    }
}
