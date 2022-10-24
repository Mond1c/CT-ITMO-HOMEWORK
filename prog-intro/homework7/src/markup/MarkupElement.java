package markup;

public interface MarkupElement {
    void toHtml(StringBuilder builder);
    void toMarkdown(StringBuilder builder);
}
