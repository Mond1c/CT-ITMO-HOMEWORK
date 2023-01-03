import java.util.Scanner;
import java.util.Arrays;

public class Seven {
    private static void reverse(int[] a, int start) {
        for (int i = start, j = a.length - 1; i < j; i++, j--) {
            swap(a, i, j);
        }
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private static boolean nextPermutation(int[] a) {
        int i = a.length - 2;
        while (i >= 0 && a[i + 1] <= a[i]) {
            i--;
        }
        if (i < 0) {
            return false;
        }
        int j = a.length - 1;
        while (a[j] <= a[i]) {
            j--;
        }
        swap(a, i, j);
        reverse(a, i + 1);
        return true;
    }

    public static void main(String[] args) {
        final int n = new Scanner(System.in).nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
        }
        do {
            for (int i = 0; i < n; i++) {
                System.out.print(String.format("%d ", a[i]));
            }
            System.out.println();
        } while (nextPermutation(a));
    } 
}
