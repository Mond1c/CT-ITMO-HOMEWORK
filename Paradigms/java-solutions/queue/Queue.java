package queue;

// Model: a[1]..a[size]
// :NOTE: n
// Invariant: (for i=1..n a[i] != null) && (size >= 0)
public interface Queue {
    // immutable(n) <=> for i=1..n: a'[i] = a[i]
    // immutable(x, y) <=> for i=x..y: a'[i-x+1] = a[i]
    // immutable(x, y, start) <=> for i=x..y: a'[i-x+start] = a[i]

    // const_state <=> immutable(size) && size' = size

    // T = min{set} <=> (for i in set: T <= i) && (T in set)

    // Pre: element != null
    // Post: size' = size + 1 && a[size'] = element && immutable(size)
    void enqueue(Object element);

    // Pre: size > 0
    // Post: size' = size - 1 && immutable(2,size) && R == a[1]
    Object dequeue();

    // Pre: size > 0
    // Post: const_state && immutable(size) && size' = size
    Object element();

    // Pre: true
    // Post: R == size && const_state
    int size();

    // Pre: true
    // Post: R == (size == 0) && const_state
    boolean isEmpty();

    // Pre: true
    // Post: size' = 0
    void clear();

    // Pre: true
    // :NOTE: simplify
    // Post: R = {i: a[i] == element}.length > 0 && const_state
    boolean contains(Object element);

    // :NOTE: ?:
    // Pre: true
    // Post: R = contains(element) &&
    //         (R ?
    //              immutable(index - 1) && immutable(index + 1, size, index) && size' = size - 1
    //            : const_state)
    boolean removeFirstOccurrence(Object element);
}
