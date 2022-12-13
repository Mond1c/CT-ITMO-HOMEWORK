package expression.parser;

import java.util.ArrayList;


public class Stack<T> extends ArrayList<T> {
    public void push(T value) {
        this.add(value);
    }

    public T pop() {
        return this.remove(this.size() - 1);
    }

    public T top() {
        return this.get(this.size() - 1);
    }
}
