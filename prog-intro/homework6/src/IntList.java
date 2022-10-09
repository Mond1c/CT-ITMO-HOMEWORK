import java.util.Arrays;

public class IntList {
    private static final int SIZE_FACTOR = 2;
    private int[] arr;
    private int size;

    public IntList() {
        this.arr = new int[2];
    }

    private void resize() {
        arr = Arrays.copyOf(arr, arr.length * SIZE_FACTOR);
    }

    public void add(int var1) {
        if (size == arr.length) {
            resize();
        }

        arr[size++] = var1;
    }

    public int size() {
        return size;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of range");
        }
        return arr[index];
    }
}