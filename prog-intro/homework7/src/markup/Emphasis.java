package markup;

import java.util.List;

public class Emphasis extends ParagraphElement {
    public Emphasis(List<ParagraphElement> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<em>");
        super.toHtml(builder);
        builder.append("</em>");
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("*");
        super.toMarkdown(builder);
        builder.append("*");
    }
}
