import java.util.List;

public class Strikeout extends TextProperty implements TextElement {
    public Strikeout(List<TextElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("~");
        getMarkdownFromElements(builder);
        builder.append("~");
    }
}