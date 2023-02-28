package newQueue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {

    private class Node {
        private Object element;
        private Node next;

        public Node(final Object element) {
            this.element = element;
        }
    }

    private Node head;
    private Node tail;

    @Override
    protected void enqueueImpl(Object element) {
        if (isEmpty()) {
            head = new Node(element);
            tail = head;
        } else {
            tail.next = new Node(element);
            tail = tail.next;
        }
    }

    @Override
    protected Object dequeueImpl() {
        final Object result = head.element;
        head = head.next;
        if (isEmpty()) {
            tail = null;
        }
        return result;
    }

    @Override
    protected Object elementImpl() {
        return head.element;
    }

    @Override
    protected void clearImpl() {
        head = null;
        tail = null;
    }

    @Override
    protected LinkedQueue filterImpl(Predicate<Object> predicate) {
        LinkedQueue ans = new LinkedQueue();
        Node ptr = head;
        while (ptr != null) {
            if (predicate.test(ptr.element)) {
                ans.enqueue(ptr.element);
            }
            ptr = ptr.next;
        }
        return ans;
    }

    @Override
    protected LinkedQueue mapImpl(Function<Object, Object> function) {
        LinkedQueue ans = new LinkedQueue();
        Node ptr = head;
        while (ptr != null) {
            ans.enqueue(function.apply(ptr.element));
            ptr = ptr.next;
        }
        return ans;
    }
}
