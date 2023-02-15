package queue;

import base.ExtendedRandom;

import java.util.ArrayDeque;
import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Queues {
    private Queues() {
    }

    /* package-private */ interface QueueModel {
        @ReflectionTest.Ignore
        ArrayDeque<Object> model();

        default Object dequeue() {
            return model().removeFirst();
        }

        default int size() {
            return model().size();
        }

        default boolean isEmpty() {
            return model().isEmpty();
        }

        default void clear() {
            model().clear();
        }

        default void enqueue(final Object element) {
            model().addLast(element);
        }

        default Object element() {
            return model().getFirst();
        }
    }

    /* package-private */ interface QueueChecker<T extends QueueModel> {
        T wrap(ArrayDeque<Object> reference);

        default List<T> linearTest(final T queue, final ExtendedRandom random) {
            // Do nothing by default
            return List.of();
        }

        default void check(final T queue, final ExtendedRandom random) {
            queue.element();
        }

        default void add(final T queue, final Object element, final ExtendedRandom random) {
            queue.enqueue(element);
        }

        default Object randomElement(final ExtendedRandom random) {
            return ArrayQueueTester.ELEMENTS[random.nextInt(ArrayQueueTester.ELEMENTS.length)];
        }

        default void remove(final T queue, final ExtendedRandom random) {
            queue.dequeue();
        }

        @SuppressWarnings("unchecked")
        default T cast(final QueueModel model) {
            return (T) model;
        }
    }

    @FunctionalInterface
    protected interface Splitter<M extends QueueModel> {
        List<M> split(final QueueChecker<? extends M> tester, final M queue, final ExtendedRandom random);
    }

    @FunctionalInterface
    protected interface LinearTester<M extends QueueModel> extends Splitter<M> {
        void test(final QueueChecker<? extends M> tester, final M queue, final ExtendedRandom random);

        @Override
        default List<M> split(final QueueChecker<? extends M> tester, final M queue, final ExtendedRandom random) {
            test(tester, queue, random);
            return List.of();
        }
    }
}
