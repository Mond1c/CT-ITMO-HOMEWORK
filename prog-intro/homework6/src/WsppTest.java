package wspp;

import base.Named;
import base.Selector;

import java.util.Comparator;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WsppTest {
    private static final Named<Comparator<Map.Entry<String, Integer>>> INPUT = Named.of("", Comparator.comparingInt(e -> 0));

    private static final Named<IntFunction<IntStream>> ALL = Named.of("", size -> IntStream.range(0, size));

    public static final Selector SELECTOR = new Selector(WsppTester.class)
            .variant("Base",            WsppTester.variant(INPUT,  ALL, Named.of("", (r, w, g) -> g)))
            ;

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
