package md2html;

import java.util.ArrayList;
import java.util.List;

public class MyStack {
    private List<MarkdownElement> list;

    public MyStack() {
        list = new ArrayList<>();
    }

    public void add(MarkdownElement element) {
        list.add(element);
    }

    public MarkdownElement top() {
        return list.get(list.size() - 1);
    }

    public MarkdownElement pop() {
        MarkdownElement topElement = top();
        list.remove(list.size() - 1);
        return topElement;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
