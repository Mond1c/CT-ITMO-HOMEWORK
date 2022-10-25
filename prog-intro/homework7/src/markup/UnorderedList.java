package markup;

import java.util.List;

public class UnorderedList extends ParagraphListElement implements ListElement {
    public UnorderedList(List<ListItem> elements) {
        super(elements, "ul");
    }
}
