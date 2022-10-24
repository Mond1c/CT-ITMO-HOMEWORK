package markup;

import java.util.List;

public class ListItem {
    private final List<ListElement> items;

    public ListItem (List<ListElement> items) {
        this.items = items;
    }


    public void toHtml(StringBuilder builder) {
        builder.append("<li>");
        for (ListElement item : items) {
            item.toHtml(builder);
        }
        builder.append("</li>");
    }
}
