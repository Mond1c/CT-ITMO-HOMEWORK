package queue;

import java.util.Arrays;
import java.util.Objects;

/*
 * Model: a[1]..a[n]
 * Invariant: for i=1..n: a[i] != null
 *
 * Let immutable(start, n): for i=start..n: a'[i] == a[i]
 * 
 * Pred: element != null
 * Post: n' = n + 1 && a[n'] == element && immutable(0, n)
 *      enqueue(element)
 * 
 * Pred: n > 0
 * Post: R == a[0] && immutable(0, n) && n' = n
 *      element()
 *
 * Pred: n > 0
 * Post: n' = n - 1 && immutable(1, n) && R = a[0]
 *      dequeue()
 *
 * Pred: true
 * Post: R == n && n' == n && immutable(0, n)
 *      size()
 *
 * Pred: true
 * Post: R == (n == 0) && n' == n && immutable(0, n)
 *      isEmpty()
 *
 * Pred: true
 * Post: n' = 0 && for all i < n elements[i] = null 
 * clear()
*/

public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int size;
    private static int left;
    private static int right;

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    // enqueue(element)
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        if (right >= elements.length) {
            right = 0;
        }
        elements[right++] = element;
        size++;
        // System.err.println("Added: " + element + ", left = " + left + ", right = " + right + ", size = " + size);
        // for (int i = 0; i < elements.length; i++) {
        //     System.err.print(elements[i] + " ");
        // }
        // System.err.println();
    }

    private static void ensureCapacity(int newSize) {
        if (elements.length < newSize) {
            Object[] tmp = new Object[size * 2];
            int i = 0;
            for (int j = left; j < size; j++) {
                if (elements[j] != null) {
                    tmp[i++] = elements[j];
                }
            }
            for (int j = 0; j < left; j++) {
                if (elements[j] != null) {
                    tmp[i++] = elements[j];
                }
            }
            left = 0;
            right = size;
            elements = tmp;
        }
    }

    // Pred: n > 0
    // Post: R == a[n] && immutable(n) && n' = n
    // element()
    public static Object element() {
        assert size > 0;
        if (left >= elements.length) {
            left = 0;
        }
        return elements[left];
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(n') && R = a[n]
    // dequeue()
    public static Object dequeue() {
        assert size > 0;
        if (left >= elements.length) {
            left = 0;
        }
        size--;
        Object result = elements[left];
        elements[left++] = null;
        //System.err.println("Deleted: " + result + ", left = " + left + ", right = " + right);
        return result;
    }

    // Pred: true
    // Post: R == n && n' == n && immutable(n)
    // size()
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    // isEmpty()
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    public static void clear() {
       // System.err.println("clear");
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        size = 0;
        left = 0;
        right = 0;
    }
}
