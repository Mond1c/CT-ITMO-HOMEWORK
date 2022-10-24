package markup;

import java.util.List;

public class UnorderedList extends ParagraphListElement implements ListElement {
    public UnorderedList(List<ListItem> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<ul>");
        super.toHtml(builder);
        builder.append("</ul>");
    }
}
