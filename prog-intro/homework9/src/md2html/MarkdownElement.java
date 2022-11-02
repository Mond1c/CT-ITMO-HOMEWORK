package md2html;

import java.util.ArrayList;
import java.util.List;

public abstract class MarkdownElement implements ConvertableToHtml {
    private final List<ConvertableToHtml> elements;
    private final String htmlTag;

    public MarkdownElement(String htmlTag) {
        this.htmlTag = htmlTag;
        this.elements = new ArrayList<>();
    }

    public void add(ConvertableToHtml element) {
        elements.add(element);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<").append(htmlTag).append(">");
        for (ConvertableToHtml element : elements) {
            element.toHtml(builder);
        }
        builder.append("</").append(htmlTag).append(">");
    }
}
