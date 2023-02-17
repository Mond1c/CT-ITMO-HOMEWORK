package markdown;

public class Image extends MarkdownElement {
    private final String alt;
    private final String src;

    public Image(String alt, String src) {
        super("img", Token.IMAGE);
        this.alt = alt;
        this.src = src;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append('<').append(htmlTag).append(" alt=\'")
                .append(alt).append("\' src=\'").append(src)
                .append("\'>");
    }
}
