import java.util.Arrays;
import java.util.Scanner;

public class D1 {
    private static int counter = 0;

    private static int[] merge(int[] a, int[] b) {
        int i = 0, j = 0;
        int[] ans = new int[a.length + b.length];
        for (int k = 0; k < ans.length; k++) {
            if (i < a.length && j < b.length) {
                if (a[i] <= b[j]) {
                    ans[k] = a[i++];
                } else {
                    ans[k] = b[j++];
                    counter += (a.length - i);
                }
            } else {
                if (i < a.length) {
                    ans[k] = a[i++];
                } else {
                    ans[k] = b[j++];
                }
            }
        }
        return ans;
    }


    private static int[] mergeSort(int[] a) {
        if (a.length == 1) {
            return a;
        }
        int mid = a.length / 2;
        return merge(mergeSort(Arrays.copyOfRange(a, 0, mid)), mergeSort(Arrays.copyOfRange(a, mid, a.length)));
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        final int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        mergeSort(a);
        System.out.println(counter);
    }
}