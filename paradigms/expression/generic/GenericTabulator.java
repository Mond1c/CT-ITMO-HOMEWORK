package expression.generic;

import expression.TripleExpression;
import expression.parser.ExpressionParser;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        final Object[][][] answer = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        final TripleExpression expr = new ExpressionParser().parse(expression);
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    answer[i - x1][j - y1][k - z1] = expr.evaluate(i, j, k);
                }
            }
        }
        return answer;
    }
}
