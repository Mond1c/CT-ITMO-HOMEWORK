import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class BufScanner implements AutoCloseable {
    private final Reader reader;
    private static final int BUFFER_SIZE = 50;
    private char[] buffer = new char[BUFFER_SIZE];
    private int position;
    private int length;
    private boolean skipLF = false;

    public BufScanner(Reader reader) {
        this.reader = reader;
    }

    public void close() throws IOException {
        reader.close();
    }

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
            //printBuffer();
            return length > 0;
        }
        return true;
    }

    private boolean isSeparator(char character) {
        int type = Character.getType(character);
        return type == Character.SPACE_SEPARATOR || type == Character.LINE_SEPARATOR
            || type == Character.PARAGRAPH_SEPARATOR || type == Character.CONTROL;
    }
/*
    public boolean hasNextLine() throws IOException {
        if (!isBufferUpdated()) {
            return false;
        }
       // int n = buffer.limit() - buffer.position();
     //   System.err.println(n);
        //System.err.println(Arrays.toString(buffer));
        //System.err.println(this.position + " " + length);
       // System.err.println(buffer.position() + " " + buffer.remaining());
        for (int position = this.position; position < this.length; position++) {
            //System.err.println((int)buffer[position]);
            if (Character.getType(buffer[position]) == Character.CONTROL) {
                if (position + 1 < length 
                && Character.getType(buffer[position + 1]) == Character.CONTROL
                && (position + 2 >= length || Character.getType(buffer[position + 2]) != Character.CONTROL)) {
                    //System.err.println(123);
                    buffer[position + 1] = ' ';
                }
                return true;
            }
        }
        return false;
    }*/

    public boolean hasNext(boolean isNumber) throws IOException {
        if (!isBufferUpdated()) {
            return false;
        }

        if (isNumber) {
            for (int position = this.position; position < length; position++) {
                if (Character.isDigit(buffer[position])) {
                    return true;
                } else if (position == length - 1 && !isBufferUpdated()) {
                    return false;
                }
            }
            return false;
        }
     
        return true;
    }

    public String nextLine() throws IOException {
        if (!isBufferUpdated()) {
            return null;
        }
        if (Character.getType(buffer[position]) == Character.CONTROL && skipLF) {
            skipLF = false;
            position++;
            return "";
        }
        if (Character.getType(buffer[position]) == Character.CONTROL) {
            skipLF = true;
        }
        StringBuilder builder = new StringBuilder();
        while (position < length || isBufferUpdated()) {
            char character = buffer[position];
           // System.err.println((int)character);
            position++;
            if (Character.getType(character) != Character.CONTROL) {
                skipLF = false;
                builder.append(character);
            } else if (skipLF) {
                return "";
            } else if (!builder.isEmpty()) {
                break;
            }
        }
        
        /*int count = 0;
        while ((buffer.hasRemaining() || isBufferUpdated()) && count < 2) {
            if (Character.getType(buffer.charAt(0)) == Character.CONTROL) {
                buffer.get();
                count++;
            } else {
                break;
            }
        }*/
        //System.err.println(builder);
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
        if (builder.isEmpty()) return null;
        return builder.toString();
    }
}
