package expression.generic.types;

public interface NumberType<T extends Comparable<T>> {
    T add(T left, T right);
    T sub(T left, T right);
    T mul(T left, T right);
    T div(T left, T right);
    T mod(T left, T right);
    T negate(T num);

    T valueOf(String unparsedConst);
    T valueOf(int integerValue);

    T getZero();
}
