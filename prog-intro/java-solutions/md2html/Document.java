package md2html;

import markdown.MarkdownElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private final List<MarkdownElement> blocks;

    private Document() {
        this.blocks = new ArrayList<>();
    }

    public void addBlock(MarkdownElement block) {
        blocks.add(block);
    }

    public void writeHtmlIntoFile(final BufferedWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (MarkdownElement block : blocks) {
            builder.setLength(0);
            block.toHtml(builder);
            writer.write(builder.toString());
            writer.newLine();
        }
    }

    public static Document markdownToDocument(final BufferedReader reader) throws IOException {
        Document document = new Document();
        new Parser(reader).parse(document);
        return document;
    }
}
