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
        return element != null && !isEmpty() && containsImpl(element);
    }

    abstract protected boolean containsImpl(Object element);

    @Override
    public boolean removeFirstOccurrence(Object element) {
        return element != null && !isEmpty() && removeFirstOccurrenceImpl(element);
    }

    abstract protected boolean removeFirstOccurrenceImpl(Object element);
}
