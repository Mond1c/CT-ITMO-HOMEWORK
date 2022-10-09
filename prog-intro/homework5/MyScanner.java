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
    private boolean isForNumbers;
    private boolean isPrevWasLF;

    public MyScanner(Reader reader) {
        this.reader = reader;
    }

    public MyScanner(Reader reader, boolean isForNumbers) {
        this.reader = reader;
        this.isForNumbers = isForNumbers;
    }

    public MyScanner(InputStream stream) {
        this.reader = new InputStreamReader(stream);
    }

    public MyScanner(InputStream stream, boolean isForNumbers) {
        this.reader = new InputStreamReader(stream);
        this.isForNumbers = isForNumbers;
    }

    public MyScanner(String source) {
        this.reader = new StringReader(source);
    }

    public MyScanner(String source, boolean isForNumbers) {
        this.reader = new StringReader(source);
        this.isForNumbers = isForNumbers;
    }

    public void close() throws IOException {
        reader.close();
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

    private boolean isFits(char character) {
        if (isForNumbers) {
            if (Character.isDigit(character) || character == '-'
                || Character.toLowerCase(character) == 'o') {
                    return true;
                } 
        } else if (!isSeparator(character)) {
            return true;
        }
        return false;
    }

    public String nextLine() throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        while (position < length || isBufferUpdated()) {
            char character = buffer[position];
            position++;
            if (Character.getType(character) != Character.CONTROL) {
                builder.append(character);
                isPrevWasLF = false;
            } else if (builder.isEmpty()) {
                if (isPrevWasLF) {
                    isPrevWasLF = false;
                    return "";
                } else {
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

    public String next() throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        while (position < length || isBufferUpdated()) {
            char character = buffer[position];
            position++;
            if (isFits(character)) {
                builder.append(character);
            } else if (!builder.isEmpty()) {
                break;
            }
        }
        
        if (builder.isEmpty()) {
            return null;
        }
        return builder.toString();
    }
}
