package search;

public class BinarySearchV1 {

    // Pre: arr[i] >= arr[j] for all i < j
    // Post: a[R] <= x && R = min(a[i] <= x)
    private static int binarySearchIterative(int[] arr, int x) {
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

    // Pre: l >= 0 && r >= 0 && (arr[i] >= arr[j] for all i < j)
    // Post: a[R] <= x && R = min(a[i] <= x)
    private static int binarySearchRecursive(int[] arr, int x, int l, int r) {
        if (l >= r) {
            // l' >= r'
            return l;
        }
        // r' - l' > 0
        int m = l + (r - l) / 2;
        // r' - l' > 0 && m' = min(l', r')
        if (arr[m] <= x) {
            // r' - l' > 0 && m' = min(l', r') && arr[m'] <= x
            return binarySearchRecursive(arr, x, l, m);
        } else {
            // r' - l' > 0 && m' = min(l', r') && arr[m'] > x
            return binarySearchRecursive(arr, x, m + 1, r);
        }
    }

    // Pre: args.length > 0;
    // Post: i where a[i] <= x && i = min(a[i] <= x)
    public static void main(String[] args) {
        // args.length > 0
        final int x = Integer.parseInt(args[0]);
        // args.length > 0 && x = args[0]
        final int n = args.length - 1;
        // args.length > 0 && x = args[0] && n = args.length - 1 >= 0
        final int[] arr = new int[n];
        //args.length > 0 && x = args[0] && n = args.length - 1 >= 0 && arr = new int[n]
        for (int i = 0; i < n; ++i) {
            // i < n
            arr[i] = Integer.parseInt(args[i + 1]);
            // i < n && arr[i] = args[i + 1] && i++
        }
        System.out.println(binarySearchIterative(arr, x));
    }
}
