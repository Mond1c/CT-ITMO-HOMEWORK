package search;

public class BinarySearch {
    // P: (for all i < j < arr.length arr[i] >= arr[j]) && a[n] = -inf
    // Q: R = min(i: a[i] <= x for all 0 <= i < arr.length)
    private static int binarySearchIterative(final int[] arr, final int x) {
        // P1: P
        int l = 0, r = arr.length;
        // Q1: l = 0 && r = arr.length && l <= r
        // I: l <= r && arr[l] >= arr[r]
        while (l != r) {
            // P2: I & Q1 && l' < r'
            int m = l + (r - l) / 2;
            // Q2: l' <= m' <= r'
            // P3: P2 && Q2
            if (arr[m] <= x) {
                // P4: P3 && arr[m'] <= x
                r = m;
                // Q4: r = m'
            } else {
                // P5: P3 && arr[m'] > x
                l = m + 1;
                // Q5: l' = m' + 1
            }
        }
        // P6: l' = min(i: arr[i] <= x for all 0 <= i < arr.length)
        return l;
        // P6 -> Q
        // l is the smallest because for all 0 <= j < l: arr[j] > x
        // See P4 and P5
    }

    // P: (for all i < j < arr.length arr[i] >= arr[j] && 0 <= l <= r <= arr.length) && a[n] = -inf
    // Q: R = min(i: a[i] <= x for all 0 <= i < arr.length)
    private static int binarySearchRecursive(final int[] arr, final int x, final int l, final int r) {
        // P0: P 
        if (l == r) {
            // P && l == r
            return l;
            // l is the smallest because for all 0 <= j < l: arr[j] > x
            // See P3 and P4 
        }
        // P1: P && l' < r'
        int m = l + (r - l) / 2;
        // Q1: m' = l' + (r' - l') / 2 && l' <= m' <= r'
        // P2: P1 && Q1
        if (arr[m] <= x) {
            // P3: P2 && arr[m'] <= x
            return binarySearchRecursive(arr, x, l, m);
            // Q3: Q
        } else {
            // P4: P2 && arr[m'] > x
            return binarySearchRecursive(arr, x, m + 1, r);
            // Q4: Q
        }
    }
    
    // P: args.length > 0 && x = args[0] is a number
    //  && (args.length > 2 -> for all 0 < i < j < args.length  : 
    //          args[i] >= args[j] && args[i] is a number && args[j] is a number)
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
        // P4: Q1 && Q2 && Q3 && a[n] = -inf
        System.out.println(binarySearchIterative(arr, x));
        // Q4: Q
        
        // P5: Q1 && Q2 && Q3 && l = 0 && r = arr.length && a[n] = -ing
        // System.out.println(binarySearchRecursive(arr, x, 0, arr.length));
        // Q5: Q
    }
}
