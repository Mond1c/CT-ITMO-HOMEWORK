package markup;

import java.util.List;

public class OrderedList extends ParagraphListElement implements ListElement {
    public OrderedList(List<ListItem> elements) {
        super(elements, "ol");
    }
}
