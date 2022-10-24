package markup;

import java.util.List;

public class OrderedList extends ParagraphListElement implements ListElement {
    public OrderedList(List<ListItem> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<ol>");
        super.toHtml(builder);
        builder.append("</ol>");
    }
}
