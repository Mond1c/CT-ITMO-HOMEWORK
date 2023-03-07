package queue;


public class ArrayQueueModuleTest {
    public static void fill_tail(int size) {
        for (int i = 0; i < size; i++) {
            ArrayQueueModule.enqueue(i);
        }
    }

    public static void fill_head(int size) {
        for (int i = 0; i < size; i++) {
            ArrayQueueModule.push(i);
        }
    }

    public static void dump_head() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(
                ArrayQueueModule.size() + " " + 
                ArrayQueueModule.element() + " " + 
                ArrayQueueModule.dequeue() + " " +
                ArrayQueueModule.isEmpty());
        }
    }

    public static void dump_tail() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(
                ArrayQueueModule.size() + " " + 
                ArrayQueueModule.peek() + " " + 
                ArrayQueueModule.remove() + " " +
                ArrayQueueModule.isEmpty());
        }
    }

    public static void clear() {
        ArrayQueueModule.clear();
        System.out.println("Is queue empty: " + ArrayQueueModule.isEmpty());
    }


    public static void main(String args[]) {
        fill_head(10); dump_head();
        fill_tail(10); dump_tail();
        clear();
        fill_head(10); dump_tail();
        fill_tail(10); dump_head();
        clear();
    }
}
