package queue;

// Model: a[1]..a[size]
// Invariant: (for i=1..n a[i] != null) && (size >= 0)
public interface Queue {
    // immutable(n) <=> for i=1..n: a'[i] = a[i]
    // immutable(x, y) <=> for i=x..y: a'[i-x+1] = a[i]

    // const_state <=> immutable(size) && size' = size 

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
    // Post: R == (size == 0 ? true : false) && const_state
    boolean isEmpty();

    // Pre: true
    // Post: size' = 0
    void clear();

    // :NOTE: informal
    // :NOTE: element != null?
    // Pre: element != null
    // Post: R = (does index i exist, that a[i] = element) && const_state
    boolean contains(Object element);

    // Pre: element != null
    // :NOTE: R = contains(element) &&
    // Post: if (!contains(element)) then const_state; R = false
    // :NOTE: immutable(index + 1, size)
    //       else immutable(index - 1) && immutable(index + 1, size) && size' = size - 1,
    // :NOTE: min
    //                            where index = min{i: a[i] == element}, R = true
    boolean removeFirstOccurrence(Object element);
}
