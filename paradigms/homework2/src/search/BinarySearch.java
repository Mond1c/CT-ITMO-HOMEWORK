package search;

public class BinarySearch {

    // Pre: true
    // Post: a[R] <= x && R = min(a[i] <= x)
    private static int binarySerachIterative(int[] arr, int x) {
        // arr != null
        int l = 0, r = arr.length;
        // arr[l'] >= arr[r']
        while (l < r) {
            // arr[l'] >= arr[r'] && r' - l' > 0
            int m = l + (r - l) / 2;
            // arr[l'] >= arr[r'] && r' - l' > 0 && m' = mid(l', r')
            if (arr[m] <= x) {
                // arr[l'] >= arr[r'] && r' - l' > 0 && m' = mid(l', r') && arr[m'] <= x
                r = m;
            } else {
                // arr[l'] >= arr[r'] && r' - l' > 0 && m' = mid(l', r') && arr[m'] > x
                l = m + 1;
            }
            // arr[l'] >= arr[r']
        }
        return l;
    }

    private static int binarySearchRecursive(int[] arr, int x, int l, int r) {
        if (l >= r) {
            return l;
        }
        int m = l + (r - l) / 2;
        if (arr[m] <= x) {
            return binarySearchRecursive(arr, x, l, m);
        } else {
            return binarySearchRecursive(arr, x, m + 1, r);
        }
    }

    public static void main(String[] args) {
        final int x = Integer.parseInt(args[0]);
        final int n = args.length - 1;
        final int[] arr = new int[n];
        for (int i = 0; i < n; ++i) {
            arr[i] = Integer.parseInt(args[i + 1]);
        }
        System.out.println(binarySerachIterative(arr, x));
    }
}
