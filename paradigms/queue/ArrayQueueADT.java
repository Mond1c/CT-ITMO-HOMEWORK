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
    private Object[] elements = new Object[2];
    private int size;
    private int left;
    private int right;

    // Pred: true
    // Post: R = new ArrayQueueADT()
    // create()
    public static ArrayQueueADT create() {
        final ArrayQueueADT queue = new ArrayQueueADT();
        return queue;
    }

    public static Object[] toArray(final ArrayQueueADT queue) {
        final Object[] arr = new Object[queue.size];
        if (queue.left < queue.right) {
            for (int i = queue.left; i <= queue.right; i++) {
                arr[i - queue.left] = queue.elements[i];
            }
        }
        return arr;
    }

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(0, n)
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

    public static Object peek(final ArrayQueueADT deque) {
        assert deque.size > 0;
        if (deque.right < 0) {
            deque.right = deque.elements.length - 1;
        }
        return deque.elements[deque.right];
    }

    public static Object remove(final ArrayQueueADT deque) {
        assert deque.size > 0;
        if (deque.right < 0) {
            deque.right = deque.elements.length - 1;
        }
        deque.size--;
        Object result = deque.elements[deque.right];
        deque.elements[deque.right--] = null;
        return result;
    }


    public static void push(final ArrayQueueADT deque, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(deque, deque.size + 1);
        if (deque.left <= 0) {
            deque.left = deque.size - 1;
        }
        deque.elements[deque.left--] = element;
        deque.size++;
    }

    // Pred: newSize > 0
    // Post: n' = n * 2 && immutable(0, n)
    // ensureCapacity(newSize)
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
    // Post: R == a[0] && immutable(0, n) && n' = n
    // element()
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size > 0;
        if (queue.left >= queue.elements.length) {
            queue.left = 0;
        }
        return queue.elements[queue.left];
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(1, n) && R = a[0]
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
    // Post: R == n && n' == n && immutable(0, n)
    // size()
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(0, n)
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
