package sum;

import base.*;

import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class SumTest {
    private static final BiConsumer<Number, String> TO_STRING = (expected, out) -> Asserts.assertEquals("Sum", expected.toString(), out);

    private static final Named<Supplier<SumTester<Integer>>> BASE = Named.of("", () -> new SumTester<>(
            Integer::sum, n -> (int) n, (r, max) -> r.nextInt() % max, TO_STRING,
            10, 100, Integer.MAX_VALUE
    ));

    private static final Named<UnaryOperator<SumTester<?>>> PLAIN = Named.of("", t -> t);

    /* package-private */ static <T extends Number> Consumer<TestCounter> variant(
            final Named<Function<String, Runner>> runner,
            final Named<Supplier<SumTester<T>>> test,
            final Named<? extends Function<? super SumTester<T>, ? extends SumTester<?>>> modifier
    ) {
        return counter -> modifier.getValue().apply(test.getValue().get())
                .test("Sum" + test.getName() + modifier.getName() + runner.getName(), counter, runner.getValue());
    }

    /* package-private */ static final Named<Function<String, Runner>> RUNNER = Named.of("", Runner::args);
    public static final Selector SELECTOR = selector(SumTest.class, RUNNER);

    private SumTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final Named<Function<String, Runner>> runner) {
        return new Selector(owner)
                .variant("Base",            variant(runner, BASE, PLAIN))
                ;
    }


    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
