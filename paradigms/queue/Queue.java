package queue;

import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Model: a[1]...a[n]
 * Invariant: for i=1..n: a[i] != null
 * 
 * Let immutable(start, n): for i=start..n: a'[i] = a[i]
 * 
 * Pred: element != null
 * Post: n' == n + 1 && a'[n'] = element && immutable(0, n)
 *      enqueue(element)
 * 
 * Pred: n > 0
 * Post: n' == n - 1 && R = a[0] && a'[0] = null && immutable(1, n)
 *      dequeue
 * 
 * Pred: n > 0
 * Post: n' == n && R = a[0] && immutable(0, n)
 *      element()
 * 
 * Pred: true
 * Post: R = n == 0 && n == n' && immutable(0, n)
 *      isEmpty()
 * 
 * Pred: true
 * Post: R = n && n == n' && immutable(0, n)
 *      size()
 * 
 * Pred: true
 * Post: n' == 0 && for i=0..n: a'[i] = null  
 *      clear()
 * 
 * Pred: predicate != null
 * Post: n == n' && immutable(0, n) && R is a queue && {k = 0; for i=0..n: if predicate.test(a[i]): R[k++] = a[i]}
 *      filter(predicate)
 * 
 * Pred: function != null
 * Post: n == n' && immutable(0, n) && R is a queue && for i=0..n: R[i] = function.apply(a[i])
 *      map(function)
 */


public interface Queue {

    // Pred: element != null
    // Post: n' == n + 1 && a'[n'] = element && immutable(0, n)
    //      enqueue(element)
    void enqueue(final Object element);

    // Pred: n > 0
    // Post: n' == n - 1 && R = a[0] && a'[0] = null && immutable(1, n)
    //      dequeue
    Object dequeue();

    // Pred: n > 0
    // Post: n' == n && R = a[0] && immutable(0, n)
    //      element()
    Object element();

    // Pred: true
    // Post: R = n == 0 && n == n' && immutable(0, n)
    //      isEmpty()
    boolean isEmpty();

    // Pred: true
    // Post: R = n && n == n' && immutable(0, n)
    //      size()
    int size();

    // Pred: true
    // Post: n' == 0 && for i=0..n: a'[i] = null  
    //      clear()
    void clear();

    // Pred: predicate != null
    // Post: n == n' && immutable(0, n) && R is a queue && {k = 0; for i=0..n: if predicate.test(a[i]): R[k++] = a[i]}
    //      filter(predicate)
    Queue filter(Predicate<Object> predicate);

    // Pred: function != null
    // Post: n == n' && immutable(0, n) && R is a queue && for i=0..n: R[i] = function.apply(a[i])
    //      map(function)
    Queue map(Function<Object, Object> function);
}
