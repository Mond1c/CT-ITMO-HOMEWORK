package queue;

public class Main {
    public static void main(String[] args) {
        ArrayQueue q = new ArrayQueue();
        q.push(1);
        q.enqueue(2);
        while (!q.isEmpty()) {
            System.out.println(q.dequeue());
        }
    }
}
