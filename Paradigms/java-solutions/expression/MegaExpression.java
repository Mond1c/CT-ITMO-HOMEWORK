package expression;

public interface MegaExpression extends TripleExpression, ToMiniStringExtended {
    int getPriority();
    boolean needBracketsIfEqualPriority();
}