package queue;

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

public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int left;
    private int right;
    private int size;


    // Pred: true
    // Post: len(R) == n && (for i=0..n R[i] = a[i])
    //      toArray()   
    public static Object[] toArray(final ArrayQueueADT queue) {
        Object[] tmp = new Object[queue.size];
        int k = 0;
        for (int i = queue.left;
             i <= ((queue.left + queue.size) % queue.elements.length >= i ?
                     (queue.left + queue.size) % queue.elements.length : queue.elements.length - 1); i++) {
            if (queue.elements[i] != null) {
                tmp[k++] = queue.elements[i];
            }
        }
        for (int i = 0; i < (queue.left + queue.size >= queue.elements.length ? queue.left + queue.size - queue.elements.length : 0); i++) {
            if (queue.elements[i] != null) {
                tmp[k++] = queue.elements[i];
            }
        }
        return tmp;
    }

    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(0, n)
    //      isEmpty()
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: true
    // Post: R == n && n' == n && immutable(0, n)
    //      size()
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: newSize > 0
    // Post: elements.length' == 2 * elements.length && immutable(0, n) && n == n' 
    //      || n == n' && immutable(0, n)
    //      ensureCapacity(newSize)  
    private static void ensureCapacity(final ArrayQueueADT queue, int newSize) {
        if (newSize > queue.elements.length) {
            Object[] tmp = new Object[queue.elements.length * 2];
            System.arraycopy(queue.elements, queue.left, tmp, 0, queue.elements.length - queue.left);
            System.arraycopy(queue.elements, 0, tmp, queue.elements.length - queue.left, queue.left);
            queue.elements = tmp;
            queue.left = 0;
        }
    }

    // Pred: element != null
    // Post: n = n + 1 && a'[0] == element && (for i = 1..n+1: a'[i] = a[i - 1]) 
    //      push(element)
    public static void push(final ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);
        queue.left = (queue.left - 1 + queue.elements.length) % queue.elements.length;
        queue.elements[queue.left] = element;
        queue.size++;
    }

    //  Pred: element != null
    //  Post: n' = n + 1 && a[n'] == element && immutable(0, n)
    //       enqueue(element)
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        ensureCapacity(queue, queue.size + 1);
        queue.elements[(queue.left + queue.size) % queue.elements.length] = element;
        queue.size++;
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(1, n) && R = a[0]
    //      dequeue()
    public static Object dequeue(final ArrayQueueADT queue) {
        final Object element = queue.elements[queue.left];
        queue.elements[queue.left] = null;
        queue.left = (queue.left + 1) % queue.elements.length;
        queue.size--;
        return element;
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(0, n') && R == a[n] && a'[n] == null
    //      remove()
    public static Object remove(final ArrayQueueADT queue) {
        int right = (queue.left + queue.size - 1) % queue.elements.length;
        final Object element = queue.elements[right];
        queue.elements[right] = null;
        queue.size--;
        if (queue.left == queue.elements.length) {
            queue.left = 0;
        }
        return element;
    }

    // Pred: n > 0
    // Post: R == a[0] && immutable(0, n) && n' = n
    //      element()
    public static Object element(final ArrayQueueADT queue) {
        return queue.elements[queue.left];
    }

    // Pred: n > 0
    // Post: R == a[n] && immutable(0, n) && n' == n
    //      peek()
    public static Object peek(final ArrayQueueADT queue) {
        return queue.elements[(queue.left + queue.size + queue.elements.length - 1) % queue.elements.length];
    }

    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    public static void clear(final ArrayQueueADT queue) {
        Arrays.fill(queue.elements, queue.left, queue.elements.length, null);
        Arrays.fill(queue.elements, 0, queue.left, null);
        queue.left = 0;
        queue.size = 0;
    }
}
