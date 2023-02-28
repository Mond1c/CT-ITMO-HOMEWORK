package newQueue;

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
    private int right;

    public ArrayQueue() {
        this.elements = new Object[2];
    }

    // Pred: newSize >= n
    // Post: len(R) == newSize && (for i=0..n R[i] == a[i])
    //      copyToArray(newSize)
    private Object[] copyToArray(int newSize) {
        Object[] tmp = new Object[newSize];
        int k = 0;
        if (right > left) {
            for (int i = left; i <= right; i++) {
                if (elements[i] != null) {
                    tmp[k++] = elements[i];
                }
            }
        } else {
            for (int i = left; i < elements.length; i++) {
                if (elements[i] != null) {
                    tmp[k++] = elements[i];
                }
            }
            int bound = left == right ? right - 1 : right;
            for (int i = 0; i <= bound; i++) {
                if (elements[i] != null) {
                    tmp[k++] = elements[i];
                }
            }
        }
        return tmp;       
    }

    // Pred: true
    // Post: len(R) == n && (for i=0..n R[i] = a[i])
    //      toArray()    
    public Object[] toArray() {
        return copyToArray(size);
    }


    // Pred: newSize > 0
    // Post: elements.length' == 2 * elements.length && immutable(0, n) && n == n' 
    //      || n == n' && immutable(0, n)
    //      ensureCapacity(newSize)  
    private void ensureCapacity(int newSize) {
        if (newSize > elements.length) {
            Object[] tmp = copyToArray(elements.length * 2);
            elements = tmp;
            left = 0;
            right = size - 1;
        }
    }

    // Pred: element != null
    // Post: n = n + 1 && a'[0] == element && (for i = 1..n+1: a'[i] = a[i - 1]) 
    //      push(element) 
    public void push(final Object element) {
        ensureCapacity(size + 1);
        if (!isEmpty() && elements[left] != null) {
            left = (left + elements.length - 1) % elements.length;
        }
        if (isEmpty()) {
            right = left;
        }
        elements[left] = element;
        size++;
    }

    //  Pred: element != null
    //  Post: n' = n + 1 && a[n'] == element && immutable(0, n)
    //       enqueue(element)
    protected void enqueueImpl(final Object element) {
        ensureCapacity(size + 1);
        right = (right + 1) % elements.length;
        if (isEmpty()) {
            left = right;
        }
        elements[right] = element;
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
        final Object element = elements[right];
        elements[right] = null;
        right = (right + elements.length - 1) % elements.length;
        size--;
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
        return elements[right];
    }
    
    // Pred: true
    // Post: n' = 0 && for all i < n elements[i] = null 
    // clear()
    protected void clearImpl() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        left = 0;
        right = 0;
    }

    @Override
    protected ArrayQueue filterImpl(Predicate<Object> predicate) {
        ArrayQueue ans = new ArrayQueue();
        if (left <= right) {
            for (int i = left; i <= right; i++) {
                if (elements[i] != null && predicate.test(elements[i])) {
                    ans.enqueue(elements[i]);
                }
            }
        } else {
            for (int i = left; i < elements.length; i++) {
                if (elements[i] != null && predicate.test(elements[i])) {
                    ans.enqueue(elements[i]);
                }
            }
            for (int i = 0; i <= right; i++) {
                if (elements[i] != null && predicate.test(elements[i])) {
                    ans.enqueue(elements[i]);
                }
            }
        }
        return ans;
    }

    @Override
    protected ArrayQueue mapImpl(Function<Object, Object> function) {
        ArrayQueue ans = new ArrayQueue();
        if (left <= right) {
            for (int i = left; i <= right; i++) {
                if (elements[i] != null) {
                    ans.enqueue(function.apply(elements[i]));
                }
            }
        } else {
            for (int i = left; i < elements.length; i++) {
                if (elements[i] != null) {
                    ans.enqueue(function.apply(elements[i]));
                }
            }
            for (int i = 0; i <= right; i++) {
                if (elements[i] != null) {
                    ans.enqueue(function.apply(elements[i]));
                }
            }
        }
        return ans;
    }
}
