package expression.generic;


import expression.generic.operations.TripleExpression;
import expression.generic.parser.ExpressionParser;
import expression.generic.types.BigIntegerNumberType;
import expression.generic.types.DoubleNumberType;
import expression.generic.types.IntegerNumberType;
import expression.generic.types.NumberType;
import expression.generic.types.UIntegerNumberType;
import expression.generic.types.ULongNumberType;
import expression.generic.types.UShortNumberType;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return switch (mode) {
            case "i" -> tabulateImpl(new IntegerNumberType(), expression, x1, x2, y1, y2, z1, z2);
            case "d" -> tabulateImpl(new DoubleNumberType(), expression, x1, x2, y1, y2, z1, z2);
            case "bi" -> tabulateImpl(new BigIntegerNumberType(), expression, x1, x2, y1, y2, z1, z2);
            case "u" -> tabulateImpl(new UIntegerNumberType(), expression, x1, x2, y1, y2, z1, z2);
            case "l" -> tabulateImpl(new ULongNumberType(), expression, x1, x2, y1, y2, z1, z2);
            case "s" -> tabulateImpl(new UShortNumberType(), expression, x1, x2, y1, y2, z1, z2);
            default -> throw new IllegalArgumentException("Bad mode: " + mode);
        };
    }
    
    private <T extends Comparable<T>> Object[][][] tabulateImpl(NumberType<T> performer, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        TripleExpression<T> expr = new ExpressionParser<T>().parse(expression, performer);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = 0; x <= x2 - x1; x++) {
            for (int y = 0; y <= y2 - y1; y++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    T ix = performer.add(performer.valueOf(x), performer.valueOf(x1));
                    T iy = performer.add(performer.valueOf(y), performer.valueOf(y1));
                    T iz = performer.add(performer.valueOf(z), performer.valueOf(z1));
                    try {
                        result[x][y][z] = expr.evaluate(ix, iy, iz);
                    } catch (Exception e) {
                        result[x][y][z] = null;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        NumberType<Integer> performer = new IntegerNumberType();
        String exprStr = "1 + 2 * 3 + square 4";
        try {
            TripleExpression<Integer> expr = new ExpressionParser<Integer>().parse(exprStr, performer);
            System.out.format("%s = %s", expr.toString(), expr.evaluate(3, 0, 0).toString());
        } catch (Exception E) {
            System.out.println(E);
        }

    }
}
