package newQueue;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract void enqueueImpl(final Object element);
    protected abstract Object dequeueImpl();
    protected abstract Object elementImpl();
    protected abstract void clearImpl();
    protected abstract AbstractQueue filterImpl(Predicate<Object> predicate);
    protected abstract AbstractQueue mapImpl(Function<Object, Object> function);

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
    public AbstractQueue filter(Predicate<Object> predicate) {
        return filterImpl(predicate);
    }

    @Override
    public AbstractQueue map(Function<Object, Object> function) {
        return mapImpl(function);
    }
}
