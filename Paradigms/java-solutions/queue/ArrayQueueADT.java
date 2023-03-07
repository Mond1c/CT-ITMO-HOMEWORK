package queue;

import java.util.Objects;

// Model: a[1]..a[size]
// Invariant: (for i=1..n a[i] != null) && (size >= 0)
public class ArrayQueueADT {
    private Object[] data = new Object[5];
    private int head = 0, tail = 0;  // [head, tail)

    // immutable(n) <=> for i=1..n: a'[i] = a[i]
    // immutable(x, y) <=> for i=x..y: a'[i-x+1] = a[i]

    // Pre: true
    // Post: R = new ArrayQueueADT
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    // :NOTE: queue == null
    // Pre: element != null
    // Post: size' = size + 1 && a[size'] = element && immutable(size)
    public static void enqueue(ArrayQueueADT queue, Object element) { 
        Objects.requireNonNull(element);
        ensureCapacityToAdd(queue);
        assert queue.data[queue.tail] == null: "Trying to replace not-null";
        queue.data[queue.tail] = element;
        queue.tail = (queue.tail + 1) % queue.data.length; 
    }

    // Pre: element != null
    // Post: size' = size + 1 && a[1] = element && immutable(2, size)
    public static void push(ArrayQueueADT queue, Object element) { 
        Objects.requireNonNull(element);
        ensureCapacityToAdd(queue);
        queue.head = (queue.head - 1 + queue.data.length) % queue.data.length; 
        queue.data[queue.head] = element;
    }

    // Pre: size > 0
    // Post: size' = size - 1 && immutable(2, size) && R == a[1]
    public static Object dequeue(ArrayQueueADT queue) { 
        assert !isEmpty(queue): "Deque has no elements";
        Object result = queue.data[queue.head];
        queue.data[queue.head] = null;
        queue.head = (queue.head + 1) % queue.data.length;
        return result;
    }

    // Pre: size > 0
    // Post: size' = size - 1 && immutable(size - 1) && R == a[size]
    public static Object remove(ArrayQueueADT queue) { 
        assert !isEmpty(queue): "Deque has no elements";
        queue.tail = (queue.tail - 1 + queue.data.length) % queue.data.length;
        Object result = queue.data[queue.tail];
        queue.data[queue.tail] = null;
        return result;
    }

    // Pre: true
    // Post: size' = size && immutable(size)
    private static void ensureCapacityToAdd(ArrayQueueADT queue) {
        if (size(queue) == queue.data.length) {
            Object[] newData = copyToArray(queue, queue.data.length * 2);
            queue.head = 0; 
            queue.tail = queue.data.length;
            queue.data = newData;
        }
    }

    // Pre: newSize >= size 
    // Post: R = array of a[1]..a[size] && immutable(size) && size' = size
    private static Object[] copyToArray(ArrayQueueADT queue, int newSize) {
        assert newSize >= size(queue): "Trying to copy to smaller size";
        Object[] result = new Object[newSize];
        if (newSize != 0) {
            if (queue.head < queue.tail) {
                System.arraycopy(queue.data, queue.head, result, 0, queue.tail - queue.head);
            } else {
                System.arraycopy(queue.data, queue.head, result, 0, queue.data.length - queue.head);
                System.arraycopy(queue.data, 0, result, queue.data.length - queue.head, queue.tail);
            }
        }
        return result;
    }

    // Pre: size > 0
    // Post: R == a[1] && immutable(size) && size' = size
    public static Object element(ArrayQueueADT queue) {
        assert !isEmpty(queue): "Deque has no elements";
        return queue.data[queue.head];
    }

    // Pre: size > 0
    // Post: R == a[size] && size' = size && immutable(size)
    public static Object peek(ArrayQueueADT queue) {
        assert !isEmpty(queue): "Deque has no elements";
        return queue.data[(queue.tail - 1 + queue.data.length) % queue.data.length];
    }

    // Pre: true
    // Post: R == size && size' = size && immutable(size)
    public static int size(ArrayQueueADT queue) {
        if (isEmpty(queue)) {
            return 0;
        } else {
            return (queue.tail - queue.head == 0 ? queue.data.length : (queue.tail - queue.head + queue.data.length) % queue.data.length);
        }
    }

    // Pre: true
    // Post: R == (size == 0 ? true : false) && size' = size && immutable(size)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.data[queue.head] == null;
    }

    // Pre: true
    // Post: size' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.data = new Object[5];
        queue.head = 0; queue.tail = 0;
    }

    // Pre: true
    // Post: R = array of a[1]..a[size] && size' = size && immutable(size)
    public static Object[] toArray(ArrayQueueADT queue) {
        return copyToArray(queue, size(queue));
    }
}
