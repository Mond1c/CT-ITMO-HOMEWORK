package markup;
import java.util.List;

public class OrderedList extends MarkupListElementxxxxx {
    public OrderedList(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<ol>");
        super.toHtml(builder);
        builder.append("</ol>");
    }
}