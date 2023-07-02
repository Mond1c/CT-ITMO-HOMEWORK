package search;

public class BinarySearchUni {
    // P: arr is an array with numbers && if arr.length > 0 -> (exists i: for all 0 <= j < k < i arr[j] < arr[k] &&
    //              for all 0 <= i <= j < k < arr.length arr[j] > arr[k])
    // :NOTE: определить left part (fixed)
    // exists i:    for all 0 <= j < k < i arr[j] < arr[k] := left part of the array
    //              for all i <= j < k < arr.length arr[j] > arr[k] := right part of the array
    // Q: the smallest length of the left part of the array
    private static int binarySearchIterative(final int[] arr) {
        // P1: P
        int l = -1, r = arr.length;
        // Q1: l = 0 && r = arr.length && l <= r
        // I: arr[l'] >= arr[l' + 1] && arr[r' - (r' == len(arr))] < arr[r' + (r' != len(arr))] && (l' > l && r' < r) && -1 <= l' < r' <= len(arr)
        // :NOTE: нет связи инварианта и самой задачи, решается ли она действительно? (fixed)
        while (r - l > 1) {
            // P2: r' - l' > 1 && I
            int m = l + (r - l) / 2;
            // Q2: m' = l' + (r' - l') / 2 && l' < m' < r'
            // P3: Q2 && I
            if (m + 1 < arr.length && arr[m] <= arr[m + 1]) {
                // P4: P3 && m' + 1 < arr.length && arr[m'] <= arr[m' + 1]
                l = m;
                // Q4: l' = m' && l' > l
            } else {
                // P5: P3 && (m' + 1 >= arr.length || arr[m'] > arr[m' + 1])
                r = m;
                // Q5: r' = m' && r' < r
            }
            // (Q4 || Q5) && -1 <= l' < r' <= len(arr) && arr[l'] >= arr[l' + 1] && arr[r' - (r' == len(arr))] < arr[r' + (r' != len(arr))]

            //  we always take the half of the array -> exists k: (r - l)/2^k == 1 -> while is not infinite
            // If array is empty we have l == -1 and r == 0 and r - l == 1 -> while is not infinite
        }
        // r == l + 1 && -1 <= r < len(arr) -> r in [0, len(arr))
        // arr[l] >= arr[l + 1] && arr[l + 1] == arr[r] && arr[r] < arr[r + 1] -> r is the answer.
        return r;
    }

    // :NOTE: l = INT_MIN (fixed)
    // P: arr is an array with numbers && if arr.length > 0 -> (exists i: for all 0 <= j < k < i arr[j] < arr[k] &&
    //              for all 0 <= i <= j < k < arr.length arr[j] > arr[k]) && -1 <= l < r <= len(arr)
    // exists i:    for all 0 <= j < k < i arr[j] < arr[k] := left part of the array
    //              for all i <= j < k < arr.length arr[j] > arr[k] := right part of the array
    // Q: the smallest length of the left part of the array
    private static int binarySearchRecursive(final int[] arr, final int l, final int r) {
        // P1: P && arr[l'] >= arr[l' + 1] && arr[r' - (r' == len(arr))] < arr[r' + (r' != len(arr))] && -1 <= l' < r' <= len(arr) && (Q5 || Q6)
        if (r - l == 1) {
            // P2: r == l + 1
            return r;
            // Q2: Q 
            // (P1 && r' == l' + 1) -> arr[l'] >= arr[l' + 1] && arr[l' + 1] == arr[r'] && arr[r'] < arr[r' + 1] -> r is the answer
            // 
        }
        // P3: r' - l' < 2
        int m = l + (r - l) / 2;
        // Q3: m' = l' + (r' - l') && l' < m' < r'
        // P4: Q3
        // :NOTE: нет доказательства (fixed)
        if (m + 1 < arr.length && arr[m] <= arr[m + 1]) {
            // P5: P4 && m' + 1 < arr.length && arr[m'] <= arr[m' + 1]
            return binarySearchRecursive(arr, m, r);
            // Q5: r' == r && l' == m && arr[l'] >= arr[l'] && arr[r' - (r' == len(arr))] < arr[r' + (r' != len(arr))]
        } else {
            // P6: P4 && (m' + 1 >= arr.length || arr[m'] > arr[m' + 1])
            return binarySearchRecursive(arr, l, m);
            // Q6: l' == l && r' == m && arr[l'] >= arr[l'] && arr[r' - (r' == len(arr))] < arr[r' + (r' != len(arr))]
        }
        // Start We always take the half of the array -> exists k: (r - l)/2^k == 1 -> recursive is not infinite
        // If array is empty we have l == -1 and r == 0 and r - l == 1 -> recursive is not infinite
    }


    // :NOTE: сравнение строк (fixed)
    // P: for all 0 <= i < args.length: args[i] is an integer
    //      && if args.length > 0 -> (exists 0 <= i <= args.length: for all 0 <= j < k < i int(args[j]) < int(args[k]) && 
    //          for all i <= j < k < arr.length int(args[j]) > int(args[k]))
    // Q: the smallest length of the array 
    public static void main(String[] args) {
        // P1: args.length >= 0
        final int[] arr = new int[args.length];
        // Q1: arr is an array with length equals args.length
        // I: i <= arr.length
        for (int i = 0; i < arr.length; i++) {
            // P2: args[i'] is an integer
            arr[i] = Integer.parseInt(args[i]);
            // Q2: arr[i'] = args[i'] 

            /* P3: i' < args.length
             * i++;
             * Q3: i' = i + 1 && i' <= arr.length
             */ 
        }
        // args.length >= 0
        if (args.length % 2 == 0) {
            // P4: args.length % 2 == 0 && P
            System.out.println(binarySearchRecursive(arr, -1, arr.length));
            // Q4: Q
        } else {
            // P5: P && args.length % 2 == 1
            System.out.println(binarySearchIterative(arr));
            // Q5: Q
        }
    }
}
