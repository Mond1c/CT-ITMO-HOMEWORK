package markdown;

public class Text extends MarkdownElement {
    private final StringBuilder content;

    public Text() {
        super("", Token.TEXT);
        this.content = new StringBuilder();
    }


    @Override
    public void addString(String content) {
        this.content.append(content);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append(content);
    }
}
