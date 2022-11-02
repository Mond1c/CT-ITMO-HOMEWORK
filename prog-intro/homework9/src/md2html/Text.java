package md2html;

import java.util.List;

public class Text implements ConvertableToHtml {
    private StringBuilder builder;

    public Text() {
        this.builder = new StringBuilder();
    }

    public void add

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append(builder);
    }

}
