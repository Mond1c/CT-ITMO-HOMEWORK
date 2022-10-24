package markup;

import java.util.List;

public abstract class ParagraphListElement implements MarkupElement {
    private final List<ListItem> items;

    public ParagraphListElement(List<ListItem> elements) {
        this.items = elements;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        for (ListItem item : items) {
            item.toHtml(builder);
        }
    }

    @Override
    public void toMarkdown(StringBuilder builder) {

    }
}
