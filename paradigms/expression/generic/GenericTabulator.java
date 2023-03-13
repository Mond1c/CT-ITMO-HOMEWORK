package expression.generic;

import expression.TripleExpression;
import expression.generic.types.*;

import java.util.function.Function;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return switch (mode) {
            case "i" -> execute(expression, IntegerType::parse, x1, x2, y1, y2, z1, z2);
            case "d" -> execute(expression, DoubleType::parse, x1, x2, y1, y2, z1, z2);
            case "bi" -> execute(expression, BigIntegerType::parse, x1, x2, y1, y2, z1, z2);
            case "u" -> execute(expression, UnsafeIntegerType::parse, x1, x2, y1, y2, z1, z2);
            case "l" -> execute(expression, LongType::parse, x1, x2, y1, y2, z1, z2);
            case "s" -> execute(expression, ShortType::parse, x1, x2, y1, y2, z1, z2);
            default -> throw new IllegalArgumentException("Illegal mode: " + mode);
        };
    }

    public <T extends Number> Object[][][] execute(String expression, Function<String, Type<T>> constParser, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        PartOfExpression<T> expr = new ExpressionParser<>(constParser).parse(expression);
        Object[][][] answer = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1  + 1];
        for (int x = x1; x <= x2; x++) {
            Type<T> X = constParser.apply(String.valueOf(x));
            for (int y = y1; y <= y2; y++) {
                Type<T> Y = constParser.apply(String.valueOf(y));
                for (int z = z1; z <= z2; z++) {
                    Type<T> Z = constParser.apply(String.valueOf(z));
                    try {
                        answer[x - x1][y - y1][z - z1] = expr.evaluate(X, Y, Z).getValue();
                    } catch (ArithmeticException e) {
                        answer[x - x1][y - y1][z - z1] = null;
                    }
                }
            }
        }
        return answer;
    }
}
