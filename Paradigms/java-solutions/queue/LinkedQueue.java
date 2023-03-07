package queue;

public class LinkedQueue extends AbstractQueue {
    private static class LinkedQueueNode {
        public LinkedQueueNode(Object element, LinkedQueueNode prev) {
            this.element = element;
            this.prev = prev;
        }

        Object element;
        LinkedQueueNode prev;
    }

    LinkedQueueNode head = null, tail = null;
    int size = 0;

    @Override
    protected void enqueueImpl(Object element) {
        if (isEmpty()) {
            head = new LinkedQueueNode(element, null);
            tail = head;
        } else {
            LinkedQueueNode newTail = new LinkedQueueNode(element, null);
            tail.prev = newTail;
            tail = newTail;
        }
        size++;
    }

    @Override
    public Object dequeueImpl() {
        Object result = head.element;
        if (size() == 1) {
            head = null; tail = null; 
        } else {
            head = head.prev;
        }
        size--;
        return result;
    }

    @Override
    public Object elementImpl() {
        assert !isEmpty(): "Queue has no element.";
        return head.element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        head = null; tail = null; size = 0;
    }

    // :NOTE: copy-paste
    @Override
    protected int findElement(Object element) {
        LinkedQueueNode node = head;
        int index = 0;
        while (node != null) {
            if (node.element.equals(element)) {
                return index;
            } else {
                node = node.prev;
                index++;
            }
        }
        return -1;
    }

    @Override
    protected boolean removeFirstOccurrenceImpl(Object element) {
        if (head.element.equals(element)) {
            dequeueImpl();
            return true;
        }

        LinkedQueueNode node = head;
        while (node.prev != null) {
            if (node.prev.element.equals(element)) {
                if (tail == node.prev) {
                    tail = node;
                }
                node.prev = node.prev.prev;
                size--;
                return true;
            }
            node = node.prev;
        }
        return false;
    }
}
