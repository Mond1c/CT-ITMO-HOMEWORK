package markup;

import java.util.List;

public class Emphasis extends ParagraphElement {
    public Emphasis(List<ParagraphElement> elements) {
        super(elements, "em", "*");
    }
}
