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
        for (int i = 0; i < n; i++) {
            b[i] = a[i] - b[0];
            if (i - 1 >= 0) {
                b[i] += b[i - 1];
            }
            for (int j = 0; j < a[i]; i++) {
                f[b[i] + j] = i;
            }
        }
        final int q = scanner.nextInt();
        for (int i = 0; i < q; i++) {
            int t = scanner.nextInt();
            if (t < A) {
                System.out.println("Impossible");
                continue;
            }

        }
    }
}
