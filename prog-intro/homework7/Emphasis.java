import java.util.List;

public class Emphasis extends TextProperty implements TextElement {
    public Emphasis(List<TextElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("*");
        getMarkdownFromElements(builder);
        builder.append("*");
    }
}