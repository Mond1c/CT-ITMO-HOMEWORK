package markup;
import java.util.List;

public class Strikeout extends MarkdownElement {
    public Strikeout(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("~");
        super.toMarkdown(builder);
        builder.append("~");
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<s>");
        super.toHtml(builder);
        builder.append("</s>");
    }
}