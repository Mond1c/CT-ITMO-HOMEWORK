import java.util.List;

public abstract class TextProperty {
    private List<TextElement> elements;

    public TextProperty(List<TextElement> elements) {
        this.elements = elements;
    }

    protected void getMarkdownFromElements(StringBuilder builder) {
        for (TextElement element : elements) {
            element.toMarkdown(builder);
        }
    }
}