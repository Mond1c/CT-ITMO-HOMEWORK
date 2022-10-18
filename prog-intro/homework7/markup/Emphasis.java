package markup;
import java.util.List;

public class Emphasis extends MarkdownElement {
    public Emphasis(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("*");
        super.toMarkdown(builder);
        builder.append("*");
    }
}