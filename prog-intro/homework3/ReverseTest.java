package reverse;

import base.Named;
import base.Selector;
import base.TestCounter;
import reverse.ReverseTester.Op;

import java.util.Arrays;
import java.util.function.IntToLongFunction;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

/**
 * Tests for {@code Reverse} homework.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTest {
    @FunctionalInterface
    interface LongTernaryOperator {
        long applyAsLong(long a, long b, long c);
    }

    private static final Named<Op> REVERSE = Named.of("", ReverseTester::transform);

    public static final int MAX_SIZE = 10_000 / TestCounter.DENOMINATOR;
    private static final Named<Op> SUM = cross("Sum", 0, Long::sum, (r, c, v) -> r + c - v);

    private static long[][] cross(
            final int[][] ints,
            final IntToLongFunction map,
            final LongBinaryOperator reduce,
            final int zero,
            final LongTernaryOperator get
    ) {
        // This code is intentionally obscure
        final long[] rt = Arrays.stream(ints)
                .map(Arrays::stream)
                .mapToLong(row -> row.mapToLong(map).reduce(zero, reduce))
                .toArray();
        final long[] ct = new long[Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0)];
        Arrays.fill(ct, zero);
        Arrays.stream(ints).forEach(r -> IntStream.range(0, r.length)
                .forEach(i -> ct[i] = reduce.applyAsLong(ct[i], map.applyAsLong(r[i]))));
        return IntStream.range(0, ints.length)
                .mapToObj(r -> IntStream.range(0, ints[r].length)
                        .mapToLong(c -> get.applyAsLong(rt[r], ct[c], ints[r][c]))
                        .toArray())
                .toArray(long[][]::new);
    }

    private static Named<Op> cross(
            final String name,
            final int zero,
            final LongBinaryOperator reduce,
            final LongTernaryOperator get
    ) {
        return Named.of(name, ints -> cross(ints, n -> n, reduce, zero, get));
    }

    public static final Selector SELECTOR = selector(ReverseTest.class, MAX_SIZE);

    private ReverseTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final int maxSize) {
        return new Selector(owner)
                .variant("Base",        ReverseTester.variant(maxSize, REVERSE))
                .variant("Sum",         ReverseTester.variant(maxSize, SUM))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
