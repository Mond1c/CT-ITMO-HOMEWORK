import java.util.List;

public class Paragraph extends TextProperty implements TextElement {
    public Paragraph(List<TextElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        getMarkdownFromElements(builder);
    }
}