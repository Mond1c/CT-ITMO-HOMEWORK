package search;

public class BinarySearch {
    // Pre: args.length > 0 && 
    //      for i in [0, args.length) args[i] is parseable integer && 
    //      for i in [1, args.length - 1) Integer.parseInt(args[i]) >= Integer.parseInt(args[i + 1]) (it's in desceding order)
    // Post: print i = min{ { i: a[i] <= Integer.parseInt(a[0]) for i in [1, args.length) } ∪ { args.length } }
    public static void main(String[] args) {
        int[] numbers = new int[args.length - 1];
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(args[i + 1]);
            sum += numbers[i];
        }
        // Pre is satisfied, because of main Pre-condition.
        if (sum % 2 == 0) {
            System.out.println(recursiveBinarySearch(numbers, Integer.parseInt(args[0])));
        } else {
            System.out.println(iterativeBinarySearch(numbers, Integer.parseInt(args[0])));
        }
    }

    // For convinience, we will think that a[numbers.length] = -∞ and a[-1] = +∞.
    // Pre: a[i] >= a[i + 1] for i in [-1, numbers.length) (we don't change array, so, it's always true)
    // Post: result = min{ i: a[i] <= x for i in [0, numbers.length] }
    public static int iterativeBinarySearch(int[] numbers, int x) {
        int left = -1, right = numbers.length;

        // Inv: (a[left] > x >= a[right]) && left < right
        while (right - left > 1) {
            // Pre: (right - left > 1) && Inv
            // Post: left < mid < right && Inv
            int mid = (left + right) / 2;
            // 2 * left + 1 = left + (left + 1) < left + right < 2 * right
            // 2 * left + 1 < 2 * mid < 2 * right
            // left + 0.5 < mid < right
            // left < mid < right

            // Pre: left < mid < right && Inv
            // Post: Inv
            int value = numbers[mid];
            if (value <= x) {
                // Pre: left < mid < right && a[left] > x >= a[right] && x >= a[mid]
                right = mid;
                // Post: (a[left'] = a[left] > x >= a[mid] = a[right']) && (left' = left < mid = right')
            } else {
                // Pre: left < mid < right && a[left] > x >= a[right] && x < a[mid]
                left = mid;
                // Post: (a[left'] = a[mid] > x >= a[right] = a[right']) && (left' = mid < right = right')
            }
        }

        // (right - left <= 1) && (left < right) && (a[left] > x >= a[right])
        // (left + 1 <= right && left < right) => left + 1 = right

        // a[left] = a[right - 1] > x >= a[right]
        // So, right is the smallest index, that satisfy Post-condition.

        return right;
    }

    // For convinience, we will think that a[numbers.length] = -∞ and a[-1] = +∞.
    // Pre: a[i] >= a[i + 1] for i in [-1, numbers.length)
    // Post: result = min{ i: a[i] <= x for i in [0, numbers.length] }
    public static int recursiveBinarySearch(int[] numbers, int x) {
        // Pre-condition is satisfied:
        //      1. a[i] >= a[i + 1] for i in [-1, numbers.length)
        //      2. a[left] = +∞ > x >= a[right] = -∞
        //      3. left = -1 < 0 <= numbers.length = right 
        return recursiveBinarySearch(-1, numbers.length, x, numbers);
        // So, result of function is the answer, because of its Post-condition.
    }

    // For convinience, we will think that a[numbers.length] = -∞ and a[-1] = +∞.
    // Pre: a[i] >= a[i + 1] for i in [-1, numbers.length) && a[left] > x >= a[right] && left < right
    // Post: result = min{ i: a[i] <= x for i in [0, numbers.length] }
    private static int recursiveBinarySearch(int left, int right, int x, int[] numbers) {
        // Pre: a[left] > x >= a[right] && left < right
        // Post: a[left'] > x >= a[right'] && left + 1 = right

        if (right - left <= 1) {
            // Pre: a[left] > x >= a[right] && left < right && (left + 1 >= right)
            // Post: a[left'] > x >= a[right'] && left + 1 = right

            // (left < right) && (left + 1 >= right) => (left + 1 = right)

            return right;
        } else {
            // Pre: (right - left > 1)
            // Post: left < mid < right
            int mid = (left + right) / 2;
            //                             ▼---- (left + 1 < right), because of 'Pre'-condition
            // 2 * left + 1 = left + (left + 1) < left + right < 2 * right
            // 2 * left + 1 < 2 * mid < 2 * right
            // left + 0.5 < mid < right
            // left < mid < right

            // Pre: left < mid < right && Inv
            // Post: a[left'] > x >= a[right'] && left < right
            int value = numbers[mid];
            if (value <= x) {
                // Pre: left < mid < right && a[left] > x >= a[right] && x >= a[mid]
                right = mid;
                // Post: (a[left'] = a[left] > x >= a[mid] = a[right']) && (left' = left < mid = right')
            } else {
                // Pre: left < mid < right && a[left] > x >= a[right] && x < a[mid]
                left = mid;
                // Post: (a[left'] = a[mid] > x >= a[right] = a[right']) && (left' = mid < right = right')
            }

            // So, a[i] >= a[i + 1] is always true, because we haven't changed array
            // a[left] > x > a[right] && left < right

            // Pre-condition is satistfied. So post-condition will give answer.
            return recursiveBinarySearch(left, right, x, numbers);
        }

        // a[left] = a[right - 1] > x >= a[right]
        // So, right is the smallest index, that satisfy Post-condition.
    }
}