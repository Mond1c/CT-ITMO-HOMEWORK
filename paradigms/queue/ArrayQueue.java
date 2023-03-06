package queue;

import java.rmi.server.ObjID;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

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

public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int left;

    public ArrayQueue() {
        this.elements = new Object[2];
    }


    // Pred: true
    // Post: len(R) == n && (for i=0..n R[i] = a[i])
    //      toArray()    
    public Object[] toArray() {
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


    // Pred: newSize > 0
    // Post: elements.length' == 2 * elements.length && immutable(0, n) && n == n' 
    //      || n == n' && immutable(0, n)
    //      ensureCapacity(newSize)  
    private void ensureCapacity(int newSize) {
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
    public void push(final Object element) {
        ensureCapacity(size + 1);
        left = (left - 1 + elements.length) % elements.length;
        elements[left] = element;
    }

    //  Pred: element != null
    //  Post: n' = n + 1 && a[n'] == element && immutable(0, n)
    //       enqueue(element)
    protected void enqueueImpl(final Object element) {
        ensureCapacity(size + 1);
        elements[(left + size) % elements.length] = element;
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(1, n) && R = a[0]
    //      dequeue()
    protected Object dequeueImpl() {
        final Object element = elements[left];
        elements[left] = null;
        left = (left + 1) % elements.length;
        return element;
    }

    // Pred: n > 0
    // Post: n' = n - 1 && immutable(0, n') && R == a[n] && a'[n] == null
    //      remove()
    public Object remove() {
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
    protected Object elementImpl() {
        return elements[left];
    }

    // Pred: n > 0
    // Post: R == a[n] && immutable(0, n) && n' == n
    //      peek()
    public Object peek() {
        return elements[(left + size + elements.length - 1) % elements.length];
    }
    
    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    protected void clearImpl() {
        Arrays.fill(elements, left, elements.length, null);
        Arrays.fill(elements, 0, left, null);
        left = 0;
        size = 0;
    }

    @Override
    protected boolean containsImpl(final Object element) {
        for (int i = left; i < (left < (left + size) % elements.length ? left + size + 1 : elements.length); i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                return true;
            }
        }
        if ((left + size) % elements.length < left) {
            for (int i = 0; i <= (left + size) % elements.length ; i++) {
                if (elements[i] != null && elements[i].equals(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean removeFirstOccurrenceImpl(final Object element) {
        int k = -1;
        for (int i = left; i < (left < (left + size) % elements.length  ? left + size + 1 : elements.length); i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                k = i;
                break;
            }
        }
        if ((left + size) % elements.length < left && k == -1) {
            for (int i = 0; i <= (left + size) % elements.length; i++) {
                if (elements[i] != null && elements[i].equals(element)) {
                    k = i;
                    break;
                }
            }
        }
        if (k != -1) {
            if (k == left) {
                elements[left++] = null;
                if (left == elements.length) {
                    left = 0;
                }
                size--;
                return true;
            }
            Object[] tmp = new Object[elements.length];
            int j = 0;
            for (int i = left; i < (left < (left + size) % elements.length ? left + size + 1 : elements.length); i++) {
                if (elements[i] != null && k != i) {
                    tmp[j++] = elements[i];
                }
            }
            if ((left + size) % elements.length < left) {
                for (int i = 0; i <= (left + size) % elements.length; i++) {
                    if (elements[i] != null && i != k) {
                        tmp[j++] = elements[i];
                    }
                }
            }
            left = 0;
            size--;
            elements = tmp;
            return true;
        }
        return false;
    }
}
