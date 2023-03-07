package queue;

import java.util.Objects;

// Model: a[1]..a[size]
// Invariant: (for i=1..n a[i] != null) && (size >= 0)
public class ArrayQueueModule {
    private static Object[] data = new Object[5];
    private static int head = 0, tail = 0;  // [head, tail)

    // immutable(n) <=> for i=1..n: a'[i] = a[i]
    // immutable(x, y) <=> for i=x..y: a'[i-x+1] = a[i]
    
    // Pre: element != null
    // Post: size' = size + 1 && a[size'] = element && immutable(size)
    public static void enqueue(Object element) { 
        Objects.requireNonNull(element);
        ensureCapacityToAdd();
        assert data[tail] == null: "Trying to replace not-null";
        data[tail] = element;
        tail = (tail + 1) % data.length; 
    }

    // Pre: element != null
    // Post: size' = size + 1 && a[1] = element && immutable(2, size)
    public static void push(Object element) { 
        Objects.requireNonNull(element);
        ensureCapacityToAdd();
        head = (head - 1 + data.length) % data.length; 
        data[head] = element;
    }

    // Pre: size > 0
    // Post: size' = size - 1 && immutable(2, size) && R == a[1]
    public static Object dequeue() { 
        assert !isEmpty(): "Deque has no elements";
        Object result = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        return result;
    }

    // Pre: size > 0
    // Post: size' = size - 1 && immutable(size - 1) && R == a[size]
    public static Object remove() { 
        assert !isEmpty(): "Deque has no elements";
        tail = (tail - 1 + data.length) % data.length;
        Object result = data[tail];
        data[tail] = null;
        return result;
    }

    // Pre: true
    // Post: size' = size && immutable(size)
    private static void ensureCapacityToAdd() {
        if (size() == data.length) {
            Object[] newData = copyToArray(data.length * 2);
            head = 0; tail = data.length;
            data = newData;
        }
    }

    // Pre: newSize >= size 
    // Post: R = array of a[1]..a[size] && immutable(size) && size' = size
    private static Object[] copyToArray(int newSize) {
        assert newSize >= size(): "Trying to copy to smaller size";
        Object[] result = new Object[newSize];
        if (newSize != 0) {
            if (head < tail) {
                System.arraycopy(data, head, result, 0, tail - head);
            } else {
                System.arraycopy(data, head, result, 0, data.length - head);
                System.arraycopy(data, 0, result, data.length - head, tail);
            }
        }
        return result;
    }

    // Pre: size > 0
    // Post: R == a[1] && immutable(size) && size' = size
    public static Object element() {
        assert !isEmpty(): "Deque has no elements";
        return data[head];
    }

    // Pre: size > 0
    // Post: R == a[size] && size' = size && immutable(size)
    public static Object peek() {
        assert !isEmpty(): "Deque has no elements";
        return data[(tail - 1 + data.length) % data.length];
    }

    // Pre: true
    // Post: R == size && size' = size && immutable(size)
    public static int size() {
        if (isEmpty()) {
            return 0;
        } else {
            return (tail - head == 0 ? data.length : (tail - head + data.length) % data.length);
        }
    }

    // Pre: true
    // Post: R == (size == 0 ? true : false) && size' = size && immutable(size)
    public static boolean isEmpty() {
        return data[head] == null;
    }

    // Pre: true
    // Post: size' = 0
    public static void clear() {
        data = new Object[5];
        head = 0; tail = 0;
    }

    // Pre: true
    // Post: R = array of a[1]..a[size] && size' = size && immutable(size)
    public static Object[] toArray() {
        return copyToArray(size());
    }
}