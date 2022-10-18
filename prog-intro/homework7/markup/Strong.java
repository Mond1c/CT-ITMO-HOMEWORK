package markup;
import java.util.List;

public class Strong extends MarkdownElement {
    public Strong(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("__");
        super.toMarkdown(builder);
        builder.append("__");
    }
}