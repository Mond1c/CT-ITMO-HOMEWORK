package markup;

import java.util.List;

public class Paragraph implements ListElement {
    private final List<ParagraphElement> elements;

    public Paragraph(List<ParagraphElement> elements) {
        this.elements = elements;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        for (ParagraphElement element : elements) {
            element.toHtml(builder);
        }
    }

    public void toMarkdown(StringBuilder builder) {
        for (ParagraphElement element : elements) {
            element.toMarkdown(builder);
        }
    }
}
