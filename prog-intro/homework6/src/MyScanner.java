import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

public class MyScanner implements AutoCloseable {
    private static final int BUFFER_SIZE = 1024;
    private final Reader reader;
    private char[] buffer = new char[BUFFER_SIZE];
    private int position;
    private int length;
    private String curToken;
    private String curLine;
    private boolean isLF;
    private boolean isCRLF;
    private boolean isPrevWasLF;

    public MyScanner(Reader reader) {
        this.reader = reader;
        getPlatformLineSeparator();
    }

    public MyScanner(InputStream stream) {
        this.reader = new InputStreamReader(stream);
        getPlatformLineSeparator();
    }

    public MyScanner(String source) {
        this.reader = new StringReader(source);
        getPlatformLineSeparator();
    }

    public void close() throws IOException {
        reader.close();
    }

    private void getPlatformLineSeparator() {
        if (System.lineSeparator().length() == 2) {
            isCRLF = true;
        } else {
            isLF = true;
        }
    }

    // For debug
    private void printBuffer() {
        for (int i = 0; i < length; i++) {
            if (Character.getType(buffer[i]) == Character.CONTROL) {
                System.err.print("n ");
            } else {
                System.err.print(buffer[i] + " ");
            }
        }
        System.err.println();
    }

    private boolean isBufferUpdated() throws IOException {
        if (position >= length) {
            length = reader.read(buffer);
            position = 0;
            return length > 0;
        }
        return true;
    }

    private boolean isSeparator(char character) {
        int type = Character.getType(character);
        return type == Character.SPACE_SEPARATOR || type == Character.LINE_SEPARATOR
            || type == Character.PARAGRAPH_SEPARATOR || type == Character.CONTROL;
    }

    public boolean hasNextInt() throws IOException {
        curToken = nextToken();
        if (curToken == null) {
            return false;
        }
        for (int i = 0; i < curToken.length(); i++) {
            if (Character.isDigit(curToken.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNext() throws IOException {
        curToken = nextToken();
        if (curToken == null) {
            return false;
        }
        for (int i = 0; i < curToken.length(); i++) {
            if (!isSeparator(curToken.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNextLine() throws IOException {
        curLine = nextLineToken();
        return curLine != null;
    }

    private String nextLineToken() throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        //printBuffer();
        StringBuilder builder = new StringBuilder();
        while (position < length || isBufferUpdated()) {
            char character = buffer[position];
            position++;
            // :NOTE: Другие случаи
            if (isLF && character != '\n' || isCRLF && character != '\n' && character != '\r') {
                builder.append(character);
                if (isCRLF) {
                    isPrevWasLF = false;
                }
            } else if (builder.isEmpty()) {
                if (isLF) {
                    return "";
                } else if (isCRLF) {
                    if (isPrevWasLF) {
                        isPrevWasLF = false;
                        return "";
                    }
                    isPrevWasLF = true;
                }
            } else {
                break;
            }
        }
        
        if (builder.isEmpty()) {
            return null;
        }
        return builder.toString();
    }

    public String nextLine() {
        return curLine;
    }

    private String nextToken() throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        // :NOTE: new?
        StringBuilder builder = new StringBuilder();
        while (position < length || isBufferUpdated()) {
            char character = buffer[position++];
            if (!isSeparator(character)) {
                builder.append(character);
            } else if (!builder.isEmpty()) {
                break;
            }
        }
        
        return builder.toString();
    }

    // :NOTE: double call?
    public int nextInt() throws NumberFormatException {
        // :NOTE: NPE
        if (Character.toLowerCase(curToken.charAt(curToken.length() - 1)) == 'o') {
            return Integer.parseUnsignedInt(curToken.substring(0, curToken.length() - 1), 8);
        } else {
            return Integer.parseInt(curToken);
        }
    }

    public String next() {
        return curToken;
    }
}
