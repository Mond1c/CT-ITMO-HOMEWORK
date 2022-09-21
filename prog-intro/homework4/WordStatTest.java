package wordStat;

import base.*;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Tests for <a href="https://www.kgeorgiy.info/courses/prog-intro/homeworks.html#wordstat">Word Statistics</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/prog-intro/">Introduction to Programming</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WordStatTest {
    private static final Named<Comparator<Pair<String, Integer>>> INPUT = Named.of("Input", Comparator.comparingInt(p -> 0));

    private static final Named<Function<String, Stream<String>>> ID  = Named.of("", Stream::of);

    public static final Selector SELECTOR = new Selector(WordStatTester.class)
            .variant("Base",            WordStatTester.variant(INPUT, ID))
            ;

    private WordStatTest() {
        // Utility class
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
