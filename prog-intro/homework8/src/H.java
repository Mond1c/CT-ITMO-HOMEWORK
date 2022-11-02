import java.util.Scanner;

public class H {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        int[] a = new int[n];
        int sum_a = 0;
        int A = 0;
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            sum_a += a[i];
            A = Math.max(A, a[i]);
        }
        int[] f = new int[sum_a];
        int[] b = new int[n];
        b[0] = 0;
        for (int i = 0; i < n; i++) {
            if (i - 1 >= 0) {
                b[i] += b[i - 1] + a[i - 1];
            }
            for (int j = 0; j < a[i]; j++) {
                f[b[i] + j] = i;
            }
        }
        final int q = scanner.nextInt();
        int[] dp = new int[sum_a + 1];
        for (int i = 0; i < q; i++) {
            int t = scanner.nextInt();
            if (t < A) {
                System.out.println("Impossible");
                continue;
            }
            int j = 1;
            int k = 0;
            if (dp[t] == 0) {
                while (t + k < f.length) {
                    k = b[f[t + k]];
                    j++;
                }
                dp[t] = j;
            }
            System.out.println(dp[t]);
        }
    }
}
