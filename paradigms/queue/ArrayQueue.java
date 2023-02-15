package queue;

import java.util.Arrays;
import java.util.Objects;

/*
 * Model: a[1]..a[n]
 * Invariant: for i=1..n: a[i] != null
 *
 * Let immutable(n): for i=1..n: a'[i] == a[i]
 * 
 * Pred: element != null
 * Post: n' = n + 1 && a[n'] == element && immutable(n)
 *      enqueue(element)
 * 
 * Pred: n > 0
 * Post: R == a[n] && immutable(n) && n' = n
 *      element()
 *
 * Pred: n > 0
 * Post: n' = n - 1 && immutable(n') && R = a[n]
 *      dequeue()
 *
 * Pred: true
 * Post: R == n && n' == n && immutable(n)
 *      size()
 *
 * Pred: true
 * Post: R == (n == 0) && n' == n && immutable(n)
 *      isEmpty()
 *
 * Pred: true
 * Post: n' = 0 && for all i < n elements[i] = null 
 * clear()
*/

public class ArrayQueue {
    private Object[] elements;
    private int size;
    private int left;
    private int right;

    public ArrayQueue() {
        this.elements = new Object[2];
    }

    // Pred: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    // enqueue(element)
    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        if (right >= elements.length) {
            right = 0;
        }
        elements[right++] = element;
        size++;
    }

    private void ensureCapacity(int size) {
        if (elements.length < size) {
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
    public Object element() {
        assert size > 0;
        if (left >= elements.length) {
            left = 0;
        }
        return elements[left];
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(n') && R = a[n]
    // dequeue()
    public Object dequeue() {
        assert size > 0;
        if (left >= elements.length) {
            left = 0;
        }
        size--;
        Object result = elements[left];
        elements[left++] = null;
        return result;
    }

    // Pred: true
    // Post: R == n && n' == n && immutable(n)
    // size()
    public int size() {
        return size;
    }

    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    // isEmpty()
    public boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        size = 0;
        left = 0;
        right = 0;
    }
}
