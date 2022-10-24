package markup;

import java.util.List;

public class Strong extends ParagraphElement {

    public Strong(List<ParagraphElement> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<strong>");
        super.toHtml(builder);
        builder.append("</strong>");
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append("__");
        super.toMarkdown(builder);
        builder.append("__");
    }
}
