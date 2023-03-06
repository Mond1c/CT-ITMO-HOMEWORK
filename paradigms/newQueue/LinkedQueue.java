package newQueue;

import java.util.List;
import java.util.ArrayList;

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
        if (head == null) {
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
    protected boolean containsImpl(final Object element) {
        Node ptr = head;
        while (ptr != null) {
            if (ptr.element.equals(element)) {
                return true;
            }
            ptr = ptr.next;
        }
        return false;
    }

    @Override
    protected boolean removeFirstOccurrenceImpl(final Object element) {
        Node ptr = head;
        while (ptr != null) {
            if (ptr.element.equals(element)) {
                break;
            }
            ptr = ptr.next;
        }
        if (ptr == null) {
            return false;
        }
        size--;
        if (ptr == head) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
            return true;
        }
        Node slow = head;
        while (slow.next != ptr) {
            slow = slow.next;
        }
        if (ptr == tail) {
            tail = slow;
        }
        slow.next = ptr.next;
        ptr.next = null;
        return true;
    }
}
