import java.util.Arrays;
import java.util.Scanner;

public class A8 {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();
        final int M = scanner.nextInt();
        final int[] m = new int[N];
        for (int i = 0; i < N; i++) {
            m[i] = scanner.nextInt();
        }
        final int[][] dp = new int[N + 1][M + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                if (j >= m[i - 1]) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - m[i - 1]] + m[i - 1]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        System.out.println(dp[N][M]);
    }
}
