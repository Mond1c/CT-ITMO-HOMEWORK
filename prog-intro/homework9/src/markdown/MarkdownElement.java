package markdown;

import java.util.ArrayList;
import java.util.List;

public abstract class MarkdownElement {
    private final List<MarkdownElement> elements;
    protected final String htmlTag;
    private final Token token;

    public MarkdownElement(String htmlTag, Token token) {
        this.htmlTag = htmlTag;
        this.token = token;
        this.elements = new ArrayList<>();
    }

    public void add(MarkdownElement element) {
        this.elements.add(element);
    }

    public Token getToken() {
        return token;
    }

    public void toHtml(StringBuilder builder) {
        builder.append("<").append(htmlTag).append(">");
        for (MarkdownElement element : elements) {
            element.toHtml(builder);
        }
        builder.append("</").append(htmlTag).append(">");
    }

    public void addString(String content) {

    }
}
