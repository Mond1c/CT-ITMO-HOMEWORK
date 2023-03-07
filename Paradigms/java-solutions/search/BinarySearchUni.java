package search;

public class BinarySearchUni {
    
    // Pre: (args.length > 0) -> (args[i] is parseable integer for i in [0, args.length) && 
    //                            args is concatenation of strict increasing and strict decreasing array) 
    // Post: print min result : (for i in [0, result) args[i] =< args[i + 1]) && (for i in [result, args.length - 1) args[i] >= args[i + 1])
    public static void main(String[] args) {
        int[] numbers = new int[args.length];
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(args[i]);
            sum += numbers[i];
        }

        if (sum % 2 == 0) {
            System.out.println(recursiveBinarySearch(numbers));
        } else {
            System.out.println(iterativeBinarySearch(numbers));
        }
    }

    // For convinience, we will think that numberse[-1] = numbers[numbers.length] = numbers[numbers.length + 1] = -∞. 

    // Pre: numbers is concatenation of two arrays: first is increasing, second is decreasing.
    // Post: return min result: (for i in [0, result) numbers[i] <= numbers[i + 1]) && (for i in [result, numbers.length - 1) numbers[i] >= numbers[i + 1])
    public static int iterativeBinarySearch(int[] numbers) {
        int left = -1, right = numbers.length;

        // Inv: (left < right) && a[left] <= a[left + 1] && a[right] >= a[right + 1]
        while (right - left > 1) {
            // Pre: left + 1 < right && Inv
            // Post: left < mid < right
            int mid = (left + right) / 2;

            // 2 * left + 1 = left + (left + 1) < left + right < 2 * right
            // 2 * left + 1 < 2 * mid < 2 * right
            // left + 0.5 < mid < right
            // left < mid < right

            // Pre: left < mid < right && Inv
            // Post: Inv
            if (mid + 1 != numbers.length && numbers[mid] <= numbers[mid + 1]) {
                // Pre: left < mid < right && numbers[mid] <= numbers[mid + 1] && Inv
                left = mid;
                // Inv: 1. left' = mid      < right;
                //      2. numbers[left']  <= numbers[left' + 1], since (numbers[mid] <= numbers[mid + 1] && left' = left)
                //      3. numbers[right']  = numbers[right] >= numbers[right + 1] = numbers[right' + 1]
            } else {
                // Pre: left < mid < right && numbers[mid] > numbers[mid + 1] && Inv
                right = mid;
                // Inv: 1. right'           = mid > left
                //      2. numbers[left']   = numbers[left] <= numbers[left + 1] = numbers[left' + 1]
                //      3. numbers[right']  = numbers[mid] > numbers[mid + 1] = numbers[right' + 1]
            }
        }
        // Pre:    (left < right) && numbers[left] <= numbers[left + 1] && numbers[right] >= numbers[right + 1] && left + 1 >= right
        // is equavivalent (integer maths):
        //          left + 1 = right && numbers[left] <= numbers[left + 1] && numbers[right] >= numbers[right + 1]
        //          left + 1 = right && numbers[left] <= numbers[right] >= numbers[right + 1]

        // So, numbers[right] is edge of two arrays. For minimality of length of first array, pick numbers[0..left] as first array, 
        // numbers[right..numbers.length] as second. Then, length of first array is left + 1.
        return left + 1;
    }

    // Pre: numbers is concatenation of two arrays: first is increasing, second is decreasing.
    // Post: return result: (for i in [0, result) numbers[i] <= numbers[i + 1]) && (for i in [result, numbers.length - 1) numbers[i] >= numbers[i + 1])
    public static int recursiveBinarySearch(int[] numbers) {
        // Pre-condition is satisfied: 1. from this pre-condition
        //                             2. left = -1 < 0 <= numbers.length = right
        //                             3. a[left] = -∞ <= a[left + 1] in ℤ && a[right] = -∞ >= a[right + 1] = -∞
        return recursiveBinarySearch(-1, numbers.length, numbers);
        // Post condition is equavivalent.
    }

    // Pre: 1. numbers is concatenation of two arrays: first is increasing, second is decreasing
    //      2. left < right
    //      3. a[left] <= a[left + 1] && a[right] >= a[right + 1]
    // Post: return result: (for i in [0, result) numbers[i] <= numbers[i + 1]) && (for i in [result, numbers.length - 1) numbers[i] >= numbers[i + 1])
    private static int recursiveBinarySearch(int left, int right, int[] numbers) {
        if (right - left <= 1) {
            // Pre:    (left < right) && numbers[left] <= numbers[left + 1] && numbers[right] >= numbers[right + 1] && left + 1 >= right
            // is equavivalent (integer maths):
            //          left + 1 = right && numbers[left] <= numbers[left + 1] && numbers[right] >= numbers[right + 1]
            //          left + 1 = right && numbers[left] <= numbers[right] >= numbers[right + 1]

            // So, numbers[right] is edge of two arrays. For minimality of length of first array, pick numbers[0..left] as first array, 
            // numbers[right..numbers.length] as second. Then, length of first array is left + 1.
            return right;
        } else {
            // Pre: left + 1 < right && Pre-condition of function.
            // Post: left < mid < right && Pre-condition of function.
            int mid = (left + right) / 2;

            // 2 * left + 1 = left + (left + 1) < left + right < 2 * right
            // 2 * left + 1 < 2 * mid < 2 * right
            // left + 0.5 < mid < right
            // left < mid < right

            // Pre: left < mid < right && Pre-condition 
            // Post: Pre-condition for new left' and right'
            if (mid + 1 != numbers.length && numbers[mid] <= numbers[mid + 1]) {
                // Pre: left < mid < right && numbers[mid] <= numbers[mid + 1] && Pre-condition for previous left and right.
                left = mid;
                // Post: 1. numbers is concatination ... is true, because we haven't changed array
                //       2. left' = mid      < right;
                //       3. numbers[left']  <= numbers[left' + 1], since (numbers[mid] <= numbers[mid + 1] && left' = left)
                //       4. numbers[right']  = numbers[right] >= numbers[right + 1] = numbers[right' + 1]
            } else {
                // Pre: left < mid < right && numbers[mid] > numbers[mid + 1] && Pre-condition for previous left and right.
                right = mid;
                // Post: 1. numbers is concatination ... is true, because we haven't changed array
                //       2. right'           = mid > left
                //       3. numbers[left']   = numbers[left] <= numbers[left + 1] = numbers[left' + 1]
                //       4. numbers[right']  = numbers[mid] > numbers[mid + 1] = numbers[right' + 1]
            }

            // Pre-condition of function is true for new left' and right'
            return recursiveBinarySearch(left, right, numbers);
            // Post condition is equavivalent to this function.
        }
    }
}