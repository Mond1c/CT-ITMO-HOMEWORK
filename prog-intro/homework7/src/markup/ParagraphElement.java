package markup;

import java.util.List;

public abstract class ParagraphElement implements MarkupElement {
    private final List<ParagraphElement> elements;

    public ParagraphElement(List<ParagraphElement> elements) {
        this.elements = elements;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        for (MarkupElement element : elements) {
            element.toHtml(builder);
        }
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        for (MarkupElement element : elements) {
            element.toMarkdown(builder);
        }
    }
}
