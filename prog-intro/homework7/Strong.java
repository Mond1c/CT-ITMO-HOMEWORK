import java.util.List;

public class Strong extends TextProperty implements TextElement {
    public Strong(List<TextElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("__");
        getMarkdownFromElements(builder);
        builder.append("__");
    }
}