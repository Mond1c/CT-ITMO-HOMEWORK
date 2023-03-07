package queue;


public class ArrayQueueADTTest {
    public static void fill_tail(ArrayQueueADT queue, int size) {
        for (int i = 0; i < size; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
    }

    public static void fill_head(ArrayQueueADT queue, int size) {
        for (int i = 0; i < size; i++) {
            ArrayQueueADT.push(queue, i);
        }
    }

    public static void dump_head(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                ArrayQueueADT.size(queue) + " " + 
                ArrayQueueADT.element(queue) + " " + 
                ArrayQueueADT.dequeue(queue) + " " +
                ArrayQueueADT.isEmpty(queue));
        }
    }

    public static void dump_tail(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                ArrayQueueADT.size(queue) + " " + 
                ArrayQueueADT.peek(queue) + " " + 
                ArrayQueueADT.remove(queue) + " " +
                ArrayQueueADT.isEmpty(queue));
        }
    }

    public static void clear(ArrayQueueADT queue) {
        ArrayQueueADT.clear(queue);
        System.out.println("Is queue empty: " + ArrayQueueADT.isEmpty(queue));
    }


    public static void main(String args[]) {
        ArrayQueueADT queue = ArrayQueueADT.create();
        fill_head(queue, 10); dump_head(queue);
        fill_tail(queue, 10); dump_tail(queue);
        clear(queue);
        fill_head(queue, 10); dump_tail(queue);
        fill_tail(queue, 10); dump_head(queue);
        clear(queue);
    }
}
