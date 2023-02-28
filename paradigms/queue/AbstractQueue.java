package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract void enqueueImpl(final Object element);
    protected abstract Object dequeueImpl();
    protected abstract Object elementImpl();
    protected abstract void clearImpl();
    protected abstract boolean containsImpl(final Object element);
    protected abstract boolean removeFirstOccurrenceImpl(final Object element);

    @Override
    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }

    @Override
    public Object dequeue() {
        assert size > 0;
        final Object result = dequeueImpl();
        size--;
        return result;
    }

    @Override
    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(final Object element) {
        Objects.requireNonNull(element);
        return containsImpl(element);
    }

    @Override
    public boolean removeFirstOccurrence(final Object element) {
        Objects.requireNonNull(element);
        return removeFirstOccurrenceImpl(element);
    }
}
