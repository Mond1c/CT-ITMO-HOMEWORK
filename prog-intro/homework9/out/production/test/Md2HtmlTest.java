package md2html;

import base.Selector;

import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Md2HtmlTest {

    private static final Consumer<? super Md2HtmlTester> IMAGE = tester -> tester
            .test("![картинок](http://www.ifmo.ru/images/menu/small/p10.jpg)", "<p><img alt='картинок' src='http://www.ifmo.ru/images/menu/small/p10.jpg'></p>")
            .test("![картинка](https://kgeorgiy.info)", "<p><img alt='картинка' src='https://kgeorgiy.info'></p>")
            .test("![картинка с __псевдо-выделением__](https://kgeorgiy.info)", "<p><img alt='картинка с __псевдо-выделением__' src='https://kgeorgiy.info'></p>")
            .addElement("img", "![", (checker, markup, input, output) -> {
                final StringBuilder alt = new StringBuilder();
                final StringBuilder src = new StringBuilder();

                checker.generate(markup, alt, new StringBuilder());
                checker.generate(markup, src, new StringBuilder());

                input.append("![").append(alt).append("](").append(src).append(')');
                output.append("<img alt='").append(alt).append("' src='").append(src).append("'>");
            });

    public static final Selector SELECTOR = Selector.composite(FullMd2HtmlTest.class, c -> new Md2HtmlTester(), Md2HtmlTester::test)
            .variant("Base")
            .variant("Image", IMAGE)
            .selector();

    private Md2HtmlTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
