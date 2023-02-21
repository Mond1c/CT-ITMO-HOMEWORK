package search;

public class BinarySearchUni {
    // P: arr is an array with numbers && if arr.length > 0 -> (exists i: for all 0 <= j < k < i arr[j] < arr[k] &&s
    //              for all i <= j < k < arr.length arr[j] > arr[k])
    // Q: the smallest length of the left part of the array
    private static int binarySearchIterative(final int[] arr) {
        // P1: P
        int l = 0, r = arr.length;
        // Q1: l = 0 && r = arr.length && l <= r
        // I: l <= r
        while (l != r) {
            // P2: l' <  r'
            int m = l + (r - l) / 2;
            // Q2: l' <= m' < r'
            // P3: Q2
            if (m + 1 < arr.length && arr[m] <= arr[m + 1]) {
                // P4: P3 && m' + 1 < arr.length && arr[m'] <= arr[m' + 1]
                l = m + 1;
                // Q4: l' = m' + 1
            } else {
                // P5: P3 && (m' + 1 >= arr.length || arr[m'] > arr[m' + 1])
                r = m;
                // Q5: r' = m'
            }
        }
        // After this while we have l: for all 0 <= j < k < l  arr[j] < arr[k] && l == r &&
        //  for all l <= j < k < arr.length arr[j] > arr[k]
        // l is the smallest because l == r
        // So l is the answer
        return l;
    }

    // P: arr is an array with numbers && if arr.length > 0 -> (exists i: for all 0 <= j < k < i arr[j] < arr[k] &&s
    //              for all i <= j < k < arr.length arr[j] > arr[k]) && l <= r
    // Q: the smallest length of the left part of the array
    private static int binarySearchRecursive(final int[] arr, final int l, final int r) {
        // P1: P
        if (l == r) {
            // P2: l == r 
            return l;
            // Q2: Q because P2
            // After this recursive function we have l: forr all 0 <= j < k < l: arr[j] < arr[k] && l == r &&
            //      for all l <= j < k < arr.length: arr[j] > arr[k]
            // l is the smallest because l == 3
            // So l is the answer
        }
        // P3: l < r
        int m = l + (r - l) / 2;
        // Q3: l <= m < r
        // P4: Q3
        if (m + 1 < arr.length && arr[m] <= arr[m + 1]) {
            // P5: P4 && m + 1 < arr.length && arr[m] <= arr[m + 1]
            return binarySearchRecursive(arr, m + 1, r);
        } else {
            // P6: P4 && (m + 1 >= arr.length || arr[m] > arr[m + 1])
            return binarySearchRecursive(arr, l, m);
        }
        // Q4: Q
    }

    // P: for all 0 <= i < args.length: args[i] is a number
    //      && if args.length > 0 -> (exists i: for all 0 <= j < k < i args[j] < args[k] && 
    //          for all i <= j < k < arr.length args[j] > args[k])
    // Q: the smallest length of the array 
    public static void main(String[] args) {
        // P1: args.length >= 0
        final int[] arr = new int[args.length];
        // Q1: arr is an array with length equals args.length
        // I: i <= arr.length
        for (int i = 0; i < arr.length; i++) {
            // P2: args[i'] is a number
            arr[i] = Integer.parseInt(args[i]);
            // Q2: arr[i'] = args[i'] 

            /* P3: i' < args.length
             * i++;
             * Q3: i' = i + 1 && i' <= arr.length
             */ 
        }
        if (args.length % 2 == 0) {
            System.out.println(binarySearchRecursive(arr, 0, arr.length));
        } else {
            System.out.println(binarySearchIterative(arr));
        }
    }
}
