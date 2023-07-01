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
            head = null;
            tail = null;
        } else {
            head = head.prev;
        }
        size--;
        return result;
    }

    @Override
    public Object elementImpl() {
        assert !isEmpty() : "Queue has no element.";
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
        head = null;
        tail = null;
        size = 0;
    }

    // Returns a Node, that Node.prev.element == element (or null, if there's no such Node)
    protected LinkedQueueNode findElementPrev(Object element) {
        LinkedQueueNode node = head;
        while (node.prev != null) {
            if (node.prev.element.equals(element)) {
                return node;
            } else {
                node = node.prev;
            }
        }
        return null;
    }

    @Override
    protected boolean removeFirstOccurrenceImpl(Object element) {
        if (head.element.equals(element)) {
            dequeueImpl();
            return true;
        }

        LinkedQueueNode prevNode = findElementPrev(element);
        if (prevNode == null) {
            return false;
        }
        if (tail == prevNode.prev) {
            tail = prevNode;
        }
        prevNode.prev = prevNode.prev.prev;
        size--;
        return true;
    }

    // :NOTE: common code
    @Override
    protected boolean containsImpl(Object element) {
        return head.element.equals(element) || findElementPrev(element) != null;
    }
}
