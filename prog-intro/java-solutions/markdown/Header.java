package markdown;

public class Header extends MarkdownElement {
    public Header(char level) {
        super("h" + level, Token.HEADER);
    }
}
