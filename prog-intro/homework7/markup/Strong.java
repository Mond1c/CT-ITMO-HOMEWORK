package markup;
import java.util.List;

public class Strong extends MarkdownElement {
    public Strong(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("__");
        super.toMarkdown(builder);
        builder.append("__");
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<strong>");
        super.toHtml(builder);
        builder.append("</strong>");
    }
}