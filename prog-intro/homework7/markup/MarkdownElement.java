package markup;
import java.util.List;

public abstract class MarkdownElement {
    private List<MarkdownElement> elements;

    public MarkdownElement(List<MarkdownElement> elements) {
        this.elements = elements;
    }

    public void toMarkdown(StringBuilder builder) {
        for (MarkdownElement element : elements) {
            element.toMarkdown(builder);
        }
    }

    public void toHtml(StringBuilder builder) {
        if (elements == null) {
            return;
        }
        for (MarkdownElement element : elements) {
            element.toHtml(builder);
        }
    }
}