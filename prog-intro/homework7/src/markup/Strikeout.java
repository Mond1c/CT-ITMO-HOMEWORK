package markup;

import java.util.List;

public class Strikeout extends ParagraphElement {

    public Strikeout(List<ParagraphElement> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<s>");
        super.toHtml(builder);
        builder.append("</s>");
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("~");
        super.toMarkdown(builder);
        builder.append("~");
    }
}
