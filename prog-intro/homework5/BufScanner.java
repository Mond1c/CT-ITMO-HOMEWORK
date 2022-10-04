import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

public class BufScanner implements AutoCloseable {
    private final Reader reader;
    private static final int BUFFER_SIZE = 1024;
    private char[] buffer = new char[BUFFER_SIZE];
    private int position;
    private int length;
    private boolean isPrevWasLF;

    public BufScanner(Reader reader) {
        this.reader = reader;
    }

    public BufScanner(InputStream stream) {
        this.reader = new InputStreamReader(stream);
    }

    public BufScanner(String source) {
        this.reader = new StringReader(source);
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

    public String nextLine() throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        if (Character.getType(buffer[position]) == Character.CONTROL) {
            if (isPrevWasLF) {
                isPrevWasLF = false;
                position++;
                return "";
            } else {
                isPrevWasLF = true;
                position++;
            }
        }
        StringBuilder builder = new StringBuilder();
        while (position < length || isBufferUpdated()) {
            char character = buffer[position];
            position++;
            if (Character.getType(character) != Character.CONTROL) {
                builder.append(character);
                isPrevWasLF = false;
            } else if (isPrevWasLF) {
                isPrevWasLF = false;
                return "";
            } else if (!builder.isEmpty()) {
                break;
            }
        }
        
        if (builder.isEmpty()) {
            return null;
        }
        return builder.toString();
    }

    public String next(boolean isNumber) throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();

        if (isNumber) {
            while (position < length || isBufferUpdated()) {
                char character = buffer[position];
                position++;
                if (Character.isDigit(character) || (builder.isEmpty() && character == '-')) {
                    builder.append(character);
                } else if (!builder.isEmpty()) {
                    break;
                }
            }
        } else {
            while (position < length || isBufferUpdated()) {
                char character = buffer[position];
                position++;
                if (!isSeparator(character)) {
                    builder.append(character);
                } else if (!builder.isEmpty()) {
                    break;
                }
            }
        }
        if (builder.isEmpty()) {
            return null;
        }
        return builder.toString();
    }
}
