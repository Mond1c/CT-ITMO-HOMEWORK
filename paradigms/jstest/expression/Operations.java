package jstest.expression;

import java.util.Arrays;
import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Operations {
    Operation ARITH = checker -> {
        checker.unary("negate", "Negate", a -> -a, null);

        checker.any("+", "Add", 0, 2, arith(0, Double::sum));
        checker.any("-", "Subtract", 1, 2, arith(0, (a, b) -> a - b));
        checker.any("*", "Multiply", 0, 2, arith(1, (a, b) -> a * b));
        checker.any("/", "Divide", 1, 2, arith(1, (a, b) -> a / b));
    };

    Operation ONE = constant("one", 1);
    Operation TWO = constant("two", 2);

    Operation MADD = fixed("madd", "*+", 3, args -> args[0] * args[1] + args[2], null);
    Operation FLOOR = unary("floor", "_", Math::floor, null);
    Operation CEIL = unary("ceil", "^", Math::ceil, null);

    private static Operation constant(final String name, final double value) {
        return checker -> checker.constant(name, value);
    }

    private static Operation unary(final String name, final String alias, final DoubleUnaryOperator op, final int[][] simplifications) {
        return checker -> checker.unary(name, alias, op, simplifications);
    }

    private static Operation binary(final String name, final String alias, final DoubleBinaryOperator op, final int[][] simplifications) {
        return checker -> checker.binary(name, alias, op, simplifications);
    }

    private static Operation fixed(final String name, final String alias, final int arity, final BaseTester.Func f, final int[][] simplifications) {
        return checker -> checker.fixed(name, alias, arity, f, simplifications);
    }

    private static BaseTester.Func arith(final double zero, final DoubleBinaryOperator f) {
        return args -> args.length == 0 ? zero
                : args.length == 1 ? f.applyAsDouble(zero, args[0])
                : Arrays.stream(args).reduce(f).orElseThrow();
    }
}
