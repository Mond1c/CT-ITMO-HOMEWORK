package markup;

import java.util.List;

public class Strikeout extends ParagraphElement {
    public Strikeout(List<ParagraphElement> elements) {
        super(elements, "s", "~");
    }
}
