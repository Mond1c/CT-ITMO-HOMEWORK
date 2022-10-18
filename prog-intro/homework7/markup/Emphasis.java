package markup;
import java.util.List;

public class Emphasis extends MarkdownElement {
    public Emphasis(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("*");
        super.toMarkdown(builder);
        builder.append("*");
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<em>");
        super.toHtml(builder);
        builder.append("</em>");
    }
}