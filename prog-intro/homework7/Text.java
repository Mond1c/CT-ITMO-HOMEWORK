public class Text implements TextElement {
    private String content;

    public Text(String content) {
        this.content = content;
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(content);
    }
}