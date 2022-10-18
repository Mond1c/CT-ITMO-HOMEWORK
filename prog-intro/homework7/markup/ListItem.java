package markup;
import java.util.List;

public class ListItem extends MarkdownElement {
    public ListItem(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<li>");
        super.toHtml(builder);
        builder.append("</li>");
    }
}