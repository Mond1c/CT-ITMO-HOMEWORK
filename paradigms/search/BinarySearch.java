package search;

public class BinarySearch {
    // P: (for all i < j < arr.length arr[i] >= arr[j]) && a[n] = -inf
    // Q: R = min(i: arr[i] <= x for all 0 <= i < arr.length)
    private static int binarySearchIterative(final int[] arr, final int x) {
        // P1: P
        int l = -1, r = arr.length;
        // Q1: l = 0 && r = arr.length && l <= r
        // I: arr[l] > x && arr[r] <= x && l' >= l && r' <= r && -1 <= l' < r' <= len(arr)
        while (r - l > 1) {
            // P2: I & Q1 && r' - l' > 1 && I
            int m = l + (r - l) / 2;
            // Q2: m' = l' + (r' - l') / 2 && l' < m' < r'
            // P3: P2 && Q2 && I
            if (arr[m] <= x) {
                // P4: P3 && arr[m'] <= x && I
                // :NOTE: бесконечная рекурсия (fixed)
                r = m;
                // Q4: r' = m' && arr[r'] <= x
            } else {
                // P5: P3 && arr[m'] > x && I
                l = m;
                // Q5: l' = m' && arr[l'] > x
            }
        }
        // P6: 0 < r - l <= 1 && P && arr[l] > x && arr[r] <= x
        return r;
        // Q6: Q
        // P6 -> arr[l] > x <= arr[r] -> r is the answer 
    }

    // :NOTE: i=-1 (fixed)
    // P: (for all 0 <= i < j < arr.length arr[i] >= arr[j] && -1 <= l < r <= arr.length) && a[n] = -inf
    // Q: R = min(i: a[i] <= x for all 0 <= i < arr.length)
    private static int binarySearchRecursive(final int[] arr, final int x, final int l, final int r) {
        // P0: P && arr[l'] > x && arr[r'] <= x && -1 <= l' < r' <= len(arr)
        if (r - l == 1) {
            // P && l' == r'
            return r;
            // (P0 && r' - l' < 2)  -> arr[l'] > x <= arr[l'] -> r' is the answer
        }
        // P1: P && r' - l' > 1
        int m = l + (r - l) / 2;
        // :NOTE: m = r (fixed)
        // Q1: m' = l' + (r' - l') / 2 && l' < m' < r'
        // P2: P1 && Q1
        if (arr[m] <= x) {
            // P3: P2 && arr[m'] <= x
            // :NOTE: бесконечная рекурсия (fixed)
            return binarySearchRecursive(arr, x, l, m);
            // Q3: Q
        } else {
            // P4: P2 && arr[m'] > x
            return binarySearchRecursive(arr, x, m, r);
            // Q4: Q
        }
        // Start We always take half of the array -> exists k: 1/2^k == 1 -> recursive is not infinite
        // If array is empty we have l == -1 and r == 0 and r - l == 1 -> recursive is not infinite
    }

    // :NOTE: args[i] = 2^64 (fixed)
    // :NOTE: строковое сравнение (fixed)
    // P: args.length > 0 && x = args[0] is an integer
    //  && (args.length > 2 -> for all 0 < i < j < args.length  : 
    //          args[i] is an integer && args[j] is an integer && int(args[i]) >= int(args[j]))
    // Q: i: i = min(arr[j] <= x for all j < args.length - 1)
    public static void main(String[] args) {
        // P1: args.length > 0 && args[0] is a number
        final int x = Integer.parseInt(args[0]);
        // Q1: x = args[0]
        // P2: args.length - 1  >= 0
        final int[] arr = new int[args.length - 1];
        // Q2: arr = an array of args.length - 1 elements
        // I: i <= arr.length
        for (int i = 0; i < arr.length; i++) {
            // P3: i' < arr.length && (arr.length + 1 == args.length -> i' + 1 < args.length) && P
            arr[i] = Integer.parseInt(args[i + 1]);
            // Q3: arr[i'] = args[i' + 1] && (i' > 0 && P -> arr[i' - 1] >= arr[i'])
            /*  i < arr.length
             *  i++;
             *  i <= arr.length
             */
        }
        if (args.length % 2 == 0) {
            // P4: Q1 && Q2 && Q3 && l = 0 && r = arr.length && a[n] = -inf
            System.out.println(binarySearchRecursive(arr, x, -1, arr.length));
            // Q4: Q
        } else {
            // P5: Q1 && Q2 && Q3 && a[n] = -inf
            System.out.println(binarySearchIterative(arr, x));
            // Q5: Q            
        }     
    }
}
