package markup;

import java.util.List;

public abstract class ParagraphListElement implements MarkupElement {
    private final List<ListItem> items;
    private final String htmlTag;

    public ParagraphListElement(List<ListItem> elements, String htmlTag) {
        this.items = elements;
        this.htmlTag = htmlTag;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<" + htmlTag + ">");
        for (ListItem item : items) {
            item.toHtml(builder);
        }
        builder.append("</" + htmlTag + ">");
    }

    @Override
    public void toMarkdown(StringBuilder builder) {

    }
}
