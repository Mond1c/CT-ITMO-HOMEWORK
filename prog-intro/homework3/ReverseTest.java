package reverse;

import base.Named;
import base.Selector;
import base.TestCounter;
import reverse.ReverseTester.Op;

/**
 * Tests for {@code Reverse} homework.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTest {
    private static final Named<Op> REVERSE = Named.of("", ReverseTester::transform);

    public static final int MAX_SIZE = 10_000 / TestCounter.DENOMINATOR;
    public static final Selector SELECTOR = selector(ReverseTest.class, MAX_SIZE);

    private ReverseTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final int maxSize) {
        return new Selector(owner)
                .variant("Base",        ReverseTester.variant(maxSize, REVERSE))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
