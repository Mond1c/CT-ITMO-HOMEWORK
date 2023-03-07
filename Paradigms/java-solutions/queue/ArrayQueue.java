package queue;

import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private Object[] data = new Object[2];
    private int head = 0, tail = 0;  // [head, tail)

    public void enqueueImpl(Object element) { 
        Objects.requireNonNull(element);
        ensureCapacityToAdd();
        assert data[tail] == null: "Trying to replace not-null";
        data[tail] = element;
        tail = (tail + 1) % data.length; 
    }

    public void push(Object element) { 
        Objects.requireNonNull(element);
        ensureCapacityToAdd();
        head = (head - 1 + data.length) % data.length; 
        data[head] = element;
    }

    public Object dequeueImpl() { 
        Object result = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        return result;
    }

    public Object remove() { 
        assert !isEmpty(): "Deque has no elements";
        tail = (tail - 1 + data.length) % data.length;
        Object result = data[tail];
        data[tail] = null;
        return result;
    }

    private void ensureCapacityToAdd() {
        if (size() == data.length) {
            Object[] newData = copyToArray(data.length * 2);
            head = 0;
            tail = data.length;
            data = newData;
        }
    }

    private Object[] copyToArray(int newSize) {
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

    public Object elementImpl() {
        return data[head];
    }

    public Object peek() {
        assert !isEmpty(): "Deque has no elements";
        return data[(tail - 1 + data.length) % data.length];
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            return (tail - head == 0 ? data.length : (tail - head + data.length) % data.length);
        }
    }

    public boolean isEmpty() {
        return data[head] == null;
    }

    public void clear() {
        data = new Object[5];
        head = 0; tail = 0;
    }

    public Object[] toArray() {
        return copyToArray(size());
    }

    @Override
    protected int findElement(Object element) {
        if (data[head].equals(element)) {
            return 0;
        } else {
            for (int i = 1; i != size(); i++) {
                int index = (head + i) % data.length;
                if (data[index].equals(element)) {
                    return i;
                }
            }
        } 
        return -1;
    }

    @Override
    protected boolean removeFirstOccurrenceImpl(Object element) {
        int index = findElement(element);
        if (index == -1) {
            return false;
        }

        index = (index + head) % data.length;


        if (head < tail) {
            System.arraycopy(data, index + 1, data, index, tail - index);
            tail--;
            data[tail] = null;
        } else {
            if (head <= index) {
                // :NOTE: arraycopy
                for (int i = index; i > head; i--) {
                    data[i] = data[i - 1];
                }
                data[head] = null;
                head = (head + 1) % data.length;
            } else {
                System.arraycopy(data, index + 1, data, index, tail - index);
                tail = (tail - 1 + data.length) % data.length;
                data[tail] = null;
            }
        }

        return true;
    }
}
