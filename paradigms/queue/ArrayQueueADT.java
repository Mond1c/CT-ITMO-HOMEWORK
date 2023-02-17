package queue;

import java.util.Arrays;
import java.util.Objects;

import queue.ArrayQueue;

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

public class ArrayQueueADT {
    private Object[] elements;
    private int size;
    private int left;
    private int right;

    public ArrayQueueADT() {
        elements = new Object[2];
    }

    public static ArrayQueueADT create() {
        final ArrayQueueADT queue = new ArrayQueueADT();
        return queue;
    }

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    // enqueue(element)
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        if (queue.right >= queue.elements.length) {
            queue.right = 0;
        }
        queue.elements[queue.right++] = element;
        queue.size++;
    }

    private static void ensureCapacity(final ArrayQueueADT queue, int newSize) {
        if (queue.elements.length < newSize) {
            Object[] tmp = new Object[queue.size * 2];
            int i = 0;
            for (int j = queue.left; j < queue.size; j++) {
                if (queue.elements[j] != null) {
                    tmp[i++] = queue.elements[j];
                }
            }
            for (int j = 0; j < queue.left; j++) {
                if (queue.elements[j] != null) {
                    tmp[i++] = queue.elements[j];
                }
            }
            queue.left = 0;
            queue.right = queue.size;
            queue.elements = tmp;
        }
    }

    // Pred: n > 0
    // Post: R == a[n] && immutable(n) && n' = n
    // element()
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size > 0;
        if (queue.left >= queue.elements.length) {
            queue.left = 0;
        }
        return queue.elements[queue.left];
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(n') && R = a[n]
    // dequeue()
    public static Object dequeue(final ArrayQueueADT queue) {
        assert queue.size > 0;
        if (queue.left >= queue.elements.length) {
            queue.left = 0;
        }
        queue.size--;
        Object result = queue.elements[queue.left];
        queue.elements[queue.left++] = null;
        return result;
    }

    // Pred: true
    // Post: R == n && n' == n && immutable(n)
    // size()
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    // isEmpty()
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    public static void clear(final ArrayQueueADT queue) {
        for (int i = 0; i < queue.size; i++) {
            queue.elements[i] = null;
        }
        queue.size = 0;
        queue.left = 0;
        queue.right = 0;
    }
}
