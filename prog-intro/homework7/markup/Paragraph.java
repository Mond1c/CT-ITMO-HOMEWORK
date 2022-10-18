package markup;
import java.util.List;

public class Paragraph extends MarkdownElement {
    public Paragraph(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        super.toMarkdown(builder);
    }
}