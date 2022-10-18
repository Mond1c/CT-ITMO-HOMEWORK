package markup;
import java.util.List;

public class Strikeout extends MarkdownElement {
    public Strikeout(List<MarkdownElement> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("~");
        super.toMarkdown(builder);
        builder.append("~");
    }
}