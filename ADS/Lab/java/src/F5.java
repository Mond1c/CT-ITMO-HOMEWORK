import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class F5 {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            a[i] = scanner.nextInt();
        }
        int[] path = new int[n + 1];
        int[] pos = new int[n + 1];
        int len = 0;
        for (int i = 0; i < n; i++) {
            int l = 1;
            int r = len;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (a[pos[mid]] < a[i]) {
                    l = mid + 1;
                } else if (a[pos[mid]] == a[i]) {
                    l++;
                } else {
                    r = mid - 1;
                }
            }
            int newR = l;
            path[i] = pos[newR - 1];
            if (newR > len) {
                pos[newR] = i;
                len = newR;
            } else if (a[i] < a[pos[newR]]) {
                pos[newR] = i;
            }
        }

        final int[] values = new int[len];
        int k = pos[len];
        for (int i = len - 1; i >= 0; i--) {
            values[i] = n - k;
            k = path[k];
        }

        final int[] ans = new int[len];
        int p = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (a[i] == values[p]) {
                ans[p++] = n - i;
            }
        }
        System.out.println(len);
        for (int v : ans) {
            System.out.printf("%d ", v);
        }
        System.out.println();
    }
}
