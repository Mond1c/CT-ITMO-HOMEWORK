package markup;
public class Text extends MarkdownElement {
    private String content;

    public Text(String content) {
        super(null);
        this.content = content;
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(content);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append(content);
    }
}