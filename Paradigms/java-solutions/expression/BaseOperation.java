package expression;

public abstract class BaseOperation implements Operation {
    protected void appendWithBrackets(StringBuilder sb, MegaExpression operation) {
        sb.append("(");
        operation.toMiniString(sb);
        sb.append(")");
    }

    protected void appendWithoutBrackets(StringBuilder sb, MegaExpression operation) {
        operation.toMiniString(sb);
    }
}
