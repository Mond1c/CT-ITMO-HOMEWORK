import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class M {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t  = scanner.nextInt();
        while (t-- > 0) {
            int n = scanner.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = scanner.nextInt();
            }
            Map<Integer, Integer> c = new HashMap<>();
            int count = 0;
            for (int j = n - 1; j > 0; j--) {
                for (int i = 0; i < j; i++) {
                    int a_k = 2 * a[j] - a[i];
                    count += c.getOrDefault(a_k, 0);
                }
                c.put(a[j], c.getOrDefault(a[j], 0) + 1);
            }
            System.out.println(count);
        }
    }
}