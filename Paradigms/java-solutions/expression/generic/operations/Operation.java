package expression.generic.operations;

public interface Operation<T> extends TripleExpression<T>{
    String getOperationString();
}
