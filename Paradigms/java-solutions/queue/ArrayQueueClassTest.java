package queue;

public class ArrayQueueClassTest {
    public static void fill_tail(ArrayQueue queue, int size) {
        for (int i = 0; i < size; i++) {
            queue.enqueue(i);
        }
    }

    public static void fill_head(ArrayQueue queue, int size) {
        for (int i = 0; i < size; i++) {
            queue.push(i);
        }
    }

    public static void dump_head(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(
                queue.size() + " " + 
                queue.element() + " " + 
                queue.dequeue() + " " +
                queue.isEmpty());
        }
    }

    public static void dump_tail(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(
                queue.size() + " " + 
                queue.peek() + " " + 
                queue.remove() + " " +
                queue.isEmpty());
        }
    }

    public static void clear(ArrayQueue queue) {
        queue.clear();
        System.out.println("Is queue empty: " + queue.isEmpty());
    }


    public static void main(String args[]) {
        ArrayQueue queue = new ArrayQueue();
        fill_head(queue, 10); dump_head(queue);
        fill_tail(queue, 10); dump_tail(queue);
        clear(queue);
        fill_head(queue, 10); dump_tail(queue);
        fill_tail(queue, 10); dump_head(queue);
        clear(queue);
    }
}
