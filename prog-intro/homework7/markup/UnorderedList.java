package markup;
import java.util.List;

public class UnorderedList extends MakrupListElement {
    public UnorderedList(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<ul>");
        super.toHtml(builder);
        builder.append("</ul>");
    }
}