package oldQueue;

/*
 * Model: a[1]..a[n]
 * Invariant: for i=1..n: a[i] != null
 *
 * Let immutable(start, n): for i=start..n: a'[i] == a[i]
 * 
 * Pred: element != null
 * Post: n' = n + 1 && a'[n'] == element && immutable(0, n)
 *      enqueue(element)
 *
 * Pred: element != null
 * Post: n = n + 1 && a'[0] == element && (for i = 1..n+1: a'[i] = a[i - 1]) 
 *      push(element) 
 *
 * Pred: n > 0
 * Post: R == a[0] && immutable(0, n) && n' == n
 *      element()
 *
 * Pred: n > 0
 * Post: R == a[n] && immutable(0, n) && n' == n
 *      peek()
 *
 * Pred: n > 0
 * Post: n' = n - 1 && a'[0] == null && immutable(1, n) && R = a[0]
 *      dequeue()
 *
 * Pred: n > 0
 * Post: n' = n - 1 && immutable(0, n') && R == a[n] && a'[n] == null
 *      remove()
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
 *      clear()
 *
 * Pred: true
 * Post: len(R) == n && (for i=0..n R[i] = a[i])
 *      toArray()
*/

import java.util.Arrays;

public class ArrayQueueModule {
    private final static int START_CAPACITY = 8;
    private static Object[] elements = new Object[START_CAPACITY];
    private static int left;
    private static int size;


    // Pred: true
    // Post: len(R) == n && (for i=0..n R[i] = a[i])
    //      toArray()   
    public static Object[] toArray() {
        Object[] tmp = new Object[size];
        int k = 0;
        for (int i = left; i <= ((left + size) % elements.length >= i ? (left + size) % elements.length : elements.length - 1); i++) {
            if (elements[i] != null) {
                tmp[k++] = elements[i];
            }
        }
        for (int i = 0; i < (left + size >= elements.length ? left + size - elements.length : 0); i++) {
            if (elements[i] != null) {
                tmp[k++] = elements[i];
            }
        }
        return tmp;
    }

    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(0, n)
    //      isEmpty()
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: R == n && n' == n && immutable(0, n)
    //      size()
    public static int size() {
        return size;
    }

    // Pred: newSize > 0
    // Post: elements.length' == 2 * elements.length && immutable(0, n) && n == n' 
    //      || n == n' && immutable(0, n)
    //      ensureCapacity(newSize)  
    private static void ensureCapacity(int newSize) {
        if (newSize > elements.length) {
            Object[] tmp = new Object[elements.length * 2];
            System.arraycopy(elements, left, tmp, 0, elements.length - left);
            System.arraycopy(elements, 0, tmp, elements.length - left, left);
            elements = tmp;
            left = 0;
        }
    }

    // Pred: element != null
    // Post: n = n + 1 && a'[0] == element && (for i = 1..n+1: a'[i] = a[i - 1]) 
    //      push(element) 
    public static void push(final Object element) {
        ensureCapacity(size + 1);
        left = (left - 1 + elements.length) % elements.length;
        elements[left] = element;
        size++;
    }

    //  Pred: element != null
    //  Post: n' = n + 1 && a[n'] == element && immutable(0, n)
    //       enqueue(element)
    public static void enqueue(final Object element) {
        ensureCapacity(size + 1);
        elements[(left + size) % elements.length] = element;
        size++;
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(1, n) && R = a[0]
    //      dequeue()
    public static Object dequeue() {
        final Object element = elements[left];
        elements[left] = null;
        left = (left + 1) % elements.length;
        size--;
        return element;
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(0, n') && R == a[n] && a'[n] == null
    //      remove()
    public static Object remove() {
        int right = (left + size - 1) % elements.length;
        final Object element = elements[right];
        elements[right] = null;
        size--;
        if (left == elements.length) {
            left = 0;
        }
        return element;
    }

    // Pred: n > 0
    // Post: R == a[0] && immutable(0, n) && n' = n
    //      element()
    public static Object element() {
        return elements[left];
    }

    // Pred: n > 0
    // Post: R == a[n] && immutable(0, n) && n' == n
    //      peek()
    public static Object peek() {
        return elements[(left + size + elements.length - 1) % elements.length];
    }

    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    public static void clear() {
        elements = new Object[START_CAPACITY];
        left = 0;
        size = 0;
    }
}
