package queue;

public class Main {
    public static void main(String[] args) {
        ArrayQueue q = new ArrayQueue();
        q.enqueue(1);
        System.out.println(q.element());
    }
}
