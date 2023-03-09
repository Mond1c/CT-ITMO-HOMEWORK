package markup;

import java.util.List;

public class Strong extends ParagraphElement {
    public Strong(List<ParagraphElement> elements) {
        super(elements, "strong", "__");
    }
}
