package md2html;


import markdown.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final static char HEADER_START_CHARACTER = '#';
    private final static char HIGHLIGHTING_CHARACTER_1 = '_';
    private final static char HIGHLIGHTING_CHARACTER_2 = '*';
    private final static char STRIKEOUT_CHARACTER = '-';
    private final static char CODE_CHARACTER = '`';

    private final BufferedReader reader;
    private final BufferedWriter writer;

    private MyStack stack;
    private final Map<Character, String> specialSymbols = Map.of('>', "&gt;", '<', "&lt;", '&', "&amp;");

    private boolean isPrevLineWasEmpty = true;


    public Parser(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.stack = new MyStack();
    }

    private void updateStack() {
        List<MarkdownElement> elements = new ArrayList<>();
        while (!stack.isEmpty() && (stack.top().getToken() != Token.HEADER && stack.top().getToken() != Token.PARAGRAPH)) {
            elements.add(stack.pop());
        }
        for (MarkdownElement element : elements) {
            stack.top().add(element);
        }
    }

    public void parse() throws IOException {
        while (reader.ready()) {
            parseLine(reader.readLine());
        }
        updateStack();
        MarkdownElement[] data = stack.getArray();
        int size = stack.size();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.setLength(0);
            if (data[i] != null) {
                ;
                data[i].toHtml(builder);
                System.out.println(builder.toString());
                writer.write(builder.toString());
                writer.newLine();
            }
        }
        reader.close();
        writer.close();
        stack = new MyStack();
    }

    private void parseLine(String line) {
        if (line.isEmpty()) {
            isPrevLineWasEmpty = true;
            return;
        }
        int pos = 0;
        if (isPrevLineWasEmpty) {
            while (line.charAt(pos) == HEADER_START_CHARACTER) pos++;
            updateStack();
            if (pos > 0 && Character.isWhitespace(line.charAt(pos))) {
                stack.add(new Header((char) ('0' + pos++)));
            } else {
                pos = 0;
                stack.add(new Paragraph());
            }
            isPrevLineWasEmpty = false;
        }
        if (!stack.isEmpty() && stack.top().getToken() == Token.TEXT) {
            stack.top().addString("\n");
        }
        while (pos < line.length()) {
            if (isHighlightingCharacter(line.charAt(pos))) {
                if (stack.top().getToken() == Token.TEXT) {
                    MarkdownElement element = stack.pop();
                    stack.top().add(element);
                }
                if (pos + 1 < line.length() && isHighlightingCharacter(line.charAt(pos + 1))) {
                    if (!stack.isEmpty() && stack.top().getToken() == Token.STRONG) {
                        MarkdownElement element = stack.pop();
                        stack.top().add(element);
                    } else {
                        stack.add(new Strong());
                    }
                    pos += 2;
                    continue;
                }
                if (!stack.isEmpty() && stack.top().getToken() == Token.EMPHASIS) {
                    MarkdownElement element = stack.pop();
                    stack.top().add(element);
                } else {
                    stack.add(new Emphasis());
                }
                pos++;
            } else if (pos + 1 < line.length() && isStrikeout(line.charAt(pos), line.charAt(pos + 1))) {
                if (stack.top().getToken() == Token.TEXT) {
                    MarkdownElement element = stack.pop();
                    stack.top().add(element);
                }
                if (!stack.isEmpty() && stack.top().getToken() == Token.STRIKEOUT) {
                    MarkdownElement element = stack.pop();
                    stack.top().add(element);
                } else {
                    stack.add(new Strikeout());
                }
                pos += 2;
            } else if (isCode(line.charAt(pos))) {
                if (stack.top().getToken() == Token.TEXT) {
                    MarkdownElement element = stack.pop();
                    stack.top().add(element);
                }
                if (!stack.isEmpty() && stack.top().getToken() == Token.CODE) {
                    MarkdownElement element = stack.pop();
                    stack.top().add(element);
                } else {
                    stack.add(new Code());
                }
                pos++;
            } else {
                if (stack.isEmpty() || stack.top().getToken() != Token.TEXT) {
                    stack.add(new Text());
                }

                stack.top().addString(specialSymbols.getOrDefault(line.charAt(pos), String.valueOf(line.charAt(pos++))));
            }
        }

    }

    private boolean isHighlightingCharacter(char character) {
        return character == HIGHLIGHTING_CHARACTER_1 || character == HIGHLIGHTING_CHARACTER_2;
    }

    private boolean isStrikeout(char first, char second) {
        return first == STRIKEOUT_CHARACTER && second == STRIKEOUT_CHARACTER;
    }

    private boolean isCode(char character) {
        return character == CODE_CHARACTER;
    }
}

