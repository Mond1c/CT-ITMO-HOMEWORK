package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {

    @Override
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
    }

    abstract protected void enqueueImpl(Object element);

    @Override
    public Object dequeue() {
        assert !isEmpty(): "Queue has no elements";
        return dequeueImpl();
    }

    abstract protected Object dequeueImpl();

    @Override
    public Object element() {
        assert !isEmpty(): "Queue has no elements";
        return elementImpl();
    }

    abstract protected Object elementImpl();

    @Override
    public boolean contains(Object element) {
        Objects.requireNonNull(element);
        if (isEmpty()) {
            return false;
        } else {
            int index = findElement(element);
            return (index == -1 ? false : true);
        }
    }

    abstract protected int findElement(Object element);

    @Override
    public boolean removeFirstOccurrence(Object element) {
        Objects.requireNonNull(element);
        return !isEmpty() && removeFirstOccurrenceImpl(element);
    }

    abstract protected boolean removeFirstOccurrenceImpl(Object element);
}
