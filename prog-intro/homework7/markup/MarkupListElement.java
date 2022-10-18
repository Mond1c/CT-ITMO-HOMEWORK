package markup;
import java.util.List;

public abstract class MarkupListElement {
    private List<ListItem> elements;

    public MarkupListElement(List<ListItem> elements) {
        this.elements = elements;
    }

    public void toHtml(StringBuilder builder) {
        if (elements == null) {
            return;
        }
        for (ListItem element : elements) {
            element.toHtml(builder);
        }
    }
}