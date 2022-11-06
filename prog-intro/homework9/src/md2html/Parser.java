package md2html;

import markdown.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parser {
    private static final char HEADER_CHARACTER = '#';
    private static final char BLACK_SLASH = '\\';
    private static final char HIGHLIGHTING_CHARACTER_1 = '_';
    private static final char HIGHLIGHTING_CHARACTER_2 = '*';
    private static final char STRIKEOUT_CHARACTER = '-';
    private static final char CODE_CHARACTER = '`';

    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final List<MarkdownElement> blocks;
    private final StringBuilder builder;
    private final Map<Character, String> specialSymbols = Map.of('<', "&lt;", '>', "&gt;", '&', "&amp;");
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
            parseBlock(element);
        }
        for (MarkdownElement block : blocks) {
            builder.setLength(0);
            block.toHtml(builder);
            writer.write(builder.toString());
            writer.newLine();
        }
        writer.close();
        reader.close();
    }

    private void extractText() {
        if (stack.top().getToken() == Token.TEXT) {
            MarkdownElement element = stack.pop();
            stack.top().add(element);
        }
    }

    private void parseHighlighting() {
        extractText();
        if (position + 1 < builder.length() && builder.charAt(position + 1) == builder.charAt(position)) {
            if (!stack.isEmpty() && stack.top().getToken() == Token.STRONG) {
                MarkdownElement element = stack.pop();
                stack.top().add(element);
            } else {
                stack.add(new Strong());
                if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_1) {
                    highlightingCharacter1Count -= 2;
                } else {
                    highlightingCharacter2Count -= 2;
                }
            }
            position += 2;
        } else {
            if (!stack.isEmpty() && stack.top().getToken() == Token.EMPHASIS) {
                MarkdownElement element = stack.pop();
                stack.top().add(element);
            } else {
                if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_1 && highlightingCharacter1Count - 2 >= 0) {
                    stack.add(new Emphasis());
                    highlightingCharacter1Count -= 2;
                } else if (builder.charAt(position) == HIGHLIGHTING_CHARACTER_2 && highlightingCharacter2Count - 2 >= 0) {
                    stack.add(new Emphasis());
                    highlightingCharacter2Count -= 2;
                } else {
                    if (!stack.isEmpty() && stack.top().getToken() == Token.TEXT) {
                        stack.top().addString(String.valueOf(builder.charAt(position)));
                    } else {
                        stack.add(new Text());
                        stack.top().addString(String.valueOf(builder.charAt(position)));
                    }
                }
            }
            position++;
        }
    }

    private void parseStrikeout() {
        extractText();
        if (!stack.isEmpty() && stack.top().getToken() == Token.STRIKEOUT) {
            MarkdownElement element = stack.pop();
            stack.top().add(element);
        } else {
            stack.add(new Strikeout());
        }
        position += 2;
    }

    private void parseCode() {
        extractText();
        if (!stack.isEmpty() && stack.top().getToken() == Token.CODE) {
            MarkdownElement element = stack.pop();
            stack.top().add(element);
        } else {
            stack.add(new Code());
        }
        position++;
    }

    private void parseText() {
        String character = specialSymbols.getOrDefault(builder.charAt(position), String.valueOf(builder.charAt(position)));
        if (builder.charAt(position) == BLACK_SLASH) {
            character = String.valueOf(builder.charAt(position + 1));
            position++;
        }
        if (stack.isEmpty() || stack.top().getToken() != Token.TEXT) {
            stack.add(new Text());
        }
        stack.top().addString(character);
        position++;
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
            if (builder.charAt(i) == HIGHLIGHTING_CHARACTER_1 && (i - 1 < 0 || builder.charAt(i - 1) != BLACK_SLASH)) {
                highlightingCharacter1Count++;
            } else if (builder.charAt(i) == HIGHLIGHTING_CHARACTER_2 && (i - 1 < 0 || builder.charAt(i - 1) != BLACK_SLASH)) {
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
