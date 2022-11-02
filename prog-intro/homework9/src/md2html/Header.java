package md2html;

public class Header extends MarkdownElement {
    public Header(char level) {
        super("h" + level);
    }
}
