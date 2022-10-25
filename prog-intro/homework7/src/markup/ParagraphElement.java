package markup;

import java.util.List;

public abstract class ParagraphElement implements MarkupElement {
    private final List<ParagraphElement> elements;
    private final String htmlTag;
    private final String markdownTag;

    public ParagraphElement(List<ParagraphElement> elements, String htmlTag) {
        this.htmlTag = htmlTag;
        this.markdownTag = "";
        this.elements = elements;
    }

    public ParagraphElement(List<ParagraphElement> elements, String htmlTag, String markdownTag) {
        this.htmlTag = htmlTag;
        this.markdownTag = markdownTag;
        this.elements = elements;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<" + htmlTag + ">");
        for (MarkupElement element : elements) {
            element.toHtml(builder);
        }
        builder.append("</" + htmlTag + ">");
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(markdownTag);
        for (MarkupElement element : elements) {
            element.toMarkdown(builder);
        }
        builder.append(markdownTag);
    }
}
