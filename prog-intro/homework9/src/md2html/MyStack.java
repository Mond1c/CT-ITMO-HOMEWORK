package md2html;

import markdown.MarkdownElement;

import java.util.Arrays;

public class MyStack {
    private MarkdownElement[] data;
    private int size;

    public MyStack() {
        this.data = new MarkdownElement[2];
    }

    private void expandMemory() {
        data = Arrays.copyOf(data, data.length * 2);
    }

    public void add(MarkdownElement element) {
        if (size == data.length) {
            expandMemory();
        }
        data[size++] = element;
    }

    public MarkdownElement top() {
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException("Stack is empty!");
        }
        return data[size - 1];
    }

    public MarkdownElement pop() {
        MarkdownElement element = top();
        data[--size] = null;
        return element;
    }

    public MarkdownElement[] getArray() {
        return data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
