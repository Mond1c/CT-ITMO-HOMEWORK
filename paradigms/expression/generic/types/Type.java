package expression.generic.types;

public abstract class Type<T extends Number> {
    protected T value;

    public Type(T value) {
        this.value = value;
    }


    public Type<T> add(Type<T> other) {
        return toType(addImpl(other.getValue()));
    }

    public Type<T> subtract(Type<T> other) {
        return toType(subtractImpl(other.getValue()));
    }

    public Type<T> divide(Type<T> other) {
        return toType(divideImpl(other.getValue()));
    }

    public Type<T> multiply(Type<T> other) {
        return toType(multiplyImpl(other.getValue()));
    }

    public Type<T> mod(Type<T> other) {
        return toType(modImpl(other.getValue()));
    }

    public Type<T> negate() {
        return toType(negateImpl());
    }

    public Type<T> abs() {
        return toType(absImpl());
    }

    public Type<T> square() {
        return toType(multiplyImpl(value));
    }

    protected abstract T addImpl(T other);

    protected abstract T subtractImpl(T other);

    protected abstract T divideImpl(T other);

    protected abstract T multiplyImpl(T other);

    protected abstract T modImpl(T other);

    protected abstract T negateImpl();

    protected abstract T absImpl();


    public T getValue() {
        return value;
    }

    protected abstract Type<T> toType(T value);
}
