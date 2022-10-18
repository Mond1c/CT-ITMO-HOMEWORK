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
}