package md2html;

import markdown.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parser {
    private static final char HEADER_CHARACTER = '#';
    private static final char BACK_SLASH = '\\';
    private static final char HIGHLIGHTING_CHARACTER_1 = '_';
    private static final char HIGHLIGHTING_CHARACTER_2 = '*';
    private static final char STRIKEOUT_CHARACTER = '-';
    private static final char CODE_CHARACTER = '`';
    private static final String IMAGE_START = "![";
    private static final char IMAGE_ALT_END = ']';
    private static final char IMAGE_SRC_END = ')';

    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final List<MarkdownElement> blocks;
    private final StringBuilder builder;
    private final Map<Character, String> specialSymbols =
            Map.of('<', "&lt;", '>', "&gt;", '&', "&amp;");
    private int highlightingCharacter1Count;
    private int highlightingCharacter2Count;
    private int position;
    private final MyStack stack;

    public Parser(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.blocks = new ArrayList<>();
        this.builder = new StringBuilder();
        this.stack = new MyStack();
    }

    public void parse() throws IOException {
        while (reader.ready()) {
            builder.setLength(0);
            String line = reader.readLine();
            if (line.isEmpty()) {
                continue;
            }
            final MarkdownElement element;
            int count = 0;
            while (line.charAt(count) == HEADER_CHARACTER) count++;
            if (count > 0 && Character.isWhitespace(line.charAt(count))) {
                element = new Header(Character.forDigit(count, 10));
            } else {
                element = new Paragraph();
            }
            builder.append(line);
            while (reader.ready()) {
                line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                builder.append(System.lineSeparator());
                builder.append(line);
            }
            ;
            parseBlock(element);
        }
        for (MarkdownElement block : blocks) {
            builder.setLength(0);
            block.toHtml(builder);
            writer.write(builder.toString());
            writer.newLine();
        }
    }

    private void extractText() {
        if (stack.top().getToken() == Token.TEXT) {
            MarkdownElement element = stack.pop();
            if (stack.top().getToken() != Token.IMAGE) {
                stack.top().add(element);
            } else {
                stack.add(element);
            }
        }
    }

    private boolean extractToken(Token token) {
        if (!stack.isEmpty() && stack.top().getToken() == token) {
            MarkdownElement element = stack.pop();
            stack.top().add(element);
            return true;
        }
        return false;
    }

    private void parseToken(Token token, MarkdownElement newElement) {
        if (!extractToken(token)) {
            stack.add(newElement);
        }
    }

    private void updateText(String value) {
        if (stack.isEmpty() || stack.top().getToken() != Token.TEXT) {
            stack.add(new Text());
        }
        stack.top().addString(value);
    }

    private void parseHighlighting() {
        extractText();
        if (position + 1 < builder.length() &&
                builder.charAt(position + 1) == builder.charAt(position)) {
            parseToken(Token.STRONG, new Strong());
            if (stack.isEmpty() || stack.top().getToken() != Token.STRONG) {
                if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_1) {
                    highlightingCharacter1Count -= 4;
                } else {
                    highlightingCharacter2Count -= 4;
                }
            }
            position += 2;
        } else {
            if (extractToken(Token.EMPHASIS)) {
                position++;
                return;
            }
            stack.add(new Emphasis());
            if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_1 &&
                    highlightingCharacter1Count - 2 >= 0) {
                highlightingCharacter1Count -= 2;
            } else if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_2 &&
                    highlightingCharacter2Count - 2 >= 0) {
                highlightingCharacter2Count -= 2;
            } else {
                stack.pop();
                updateText(String.valueOf(builder.charAt(position)));
            }
            position++;
        }
    }


    private void parseStrikeout() {
        extractText();
        parseToken(Token.STRIKEOUT, new Strikeout());
        position += 2;
    }

    private void parseCode() {
        extractText();
        parseToken(Token.CODE, new Code());
        position++;
    }

    private void parseText() {
        String character = specialSymbols.getOrDefault(builder.charAt(position),
                String.valueOf(builder.charAt(position)));
        if (builder.charAt(position) == BACK_SLASH) {
            character = String.valueOf(builder.charAt(position + 1));
            position++;
        }
        updateText(character);
        position++;
    }

    private void parseImage() {
        extractText();
        position += 2;
        final StringBuilder src = new StringBuilder();
        final StringBuilder alt = new StringBuilder();
        while (builder.charAt(position) != IMAGE_ALT_END) {
            alt.append(builder.charAt(position++));
        }
        position += 2;
        while (builder.charAt(position) != IMAGE_SRC_END) {
            src.append(builder.charAt(position++));
        }
        position++;
        stack.add(new Image(alt.toString(), src.toString()));
    }

    private void parseBlock(final MarkdownElement block) {
        position = 0;
        while (block.getToken() == Token.HEADER && builder.charAt(position) == HEADER_CHARACTER) position++;
        if (position != 0) {
            position++;
        }
        stack.clear();
        stack.add(block);
        highlightingCharacter1Count = highlightingCharacter2Count = 0;
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == HIGHLIGHTING_CHARACTER_1 && (i - 1 < 0 || builder.charAt(i - 1) != BACK_SLASH)) {
                highlightingCharacter1Count++;
            } else if (builder.charAt(i) == HIGHLIGHTING_CHARACTER_2 && (i - 1 < 0 || builder.charAt(i - 1) != BACK_SLASH)) {
                highlightingCharacter2Count++;
            }
        }
        while (position < builder.length()) {
            if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_1 || builder.charAt(position) == HIGHLIGHTING_CHARACTER_2) {
                parseHighlighting();
            } else if (position + 1 < builder.length() && builder.charAt(position) == STRIKEOUT_CHARACTER &&
                    builder.charAt(position + 1) == STRIKEOUT_CHARACTER) {
                parseStrikeout();
            } else if (builder.charAt(position) == CODE_CHARACTER) {
                parseCode();
            } else if (position + 1 < builder.length() && builder.charAt(position) == IMAGE_START.charAt(0)
                    && builder.charAt(position + 1) == IMAGE_START.charAt(1)) {
                parseImage();
                MarkdownElement element = stack.pop();
                stack.top().add(element);
            } else {
                parseText();
            }
        }
        List<MarkdownElement> elements = new ArrayList<>();
        while (stack.size() > 1) {
            elements.add(stack.pop());
        }
        for (int i = elements.size() - 1; i >= 0; i--) {
            block.add(elements.get(i));
        }
        blocks.add(block);
    }
}
