package md2html;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Parser {
    private static final char HIGHLIGHTING_CHARACTER_1 = '_';
    private static final char HIGHLIGHTING_CHARACTER_2 = '*';
    private static final char STRIKEOUT_CHARACTER = '-';

    private final BufferedReader reader;
    private final BufferedWriter writer;

    private boolean isPrevLineWasHeader;
    private boolean isPrevLineWasParagraph;
    private boolean isPrevLineWasEmpty;

    private Header currentHeader;
    private Paragraph currentParagraph;
    private MyStack elements;
    private Text currentText;

    public Parser(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.elements = new MyStack();
    }

    private boolean isHighlightingCharacter(char character) {
        return character == HIGHLIGHTING_CHARACTER_1 || character == HIGHLIGHTING_CHARACTER_2;
    }

    private boolean isStrikeoutCharacter(char character) {
        return character == STRIKEOUT_CHARACTER;
    }

    public void parse() {

    }

    private void parseLine(String line) {
        if (line.isEmpty()) {
            isPrevLineWasEmpty = true;
            while (!elements.isEmpty()) {
                if (isPrevLineWasParagraph) {
                    currentParagraph.add(elements.pop());
                } else if (isPrevLineWasHeader) {
                    currentHeader.add(elements.pop());
                }
            }
            isPrevLineWasHeader = isPrevLineWasParagraph = false;
            return;
        }
        int position = 0;
        while (position < line.length()) {
            if (isPrevLineWasEmpty) {
                if (line.charAt(position) == '#') {
                    int level = 0;
                    while (line.charAt(position++) == '#') level++;
                    currentHeader = new Header((char)level);
                    isPrevLineWasHeader = true;
                } else {
                    currentParagraph = new Paragraph();
                    isPrevLineWasParagraph = true;
                }
                isPrevLineWasEmpty = false;
            } else {
                if (isHighlightingCharacter(line.charAt(position))) {
                    MarkdownElement element;
                    if (position + 1 < line.length() && isHighlightingCharacter(line.charAt(position + 1))) {
                        element = new Strong();
                    } else {
                        element = new Emphasis();
                    }
                    if (!elements.isEmpty()) {
                        elements.top().add(element);
                    }
                    elements.add(element);
                } else if (position + 1 < line.length() && isStrikeoutCharacter(line.charAt(position)) &&
                    isStrikeoutCharacter(line.charAt(position + 1))) {
                    MarkdownElement element = new Strikeout();
                    if (!elements.isEmpty()) {
                        elements.top().add(element);
                    }
                    elements.add(element);
                } else {
                    text.add(line.charAt(i));
                }
            }
        }
    }
}
