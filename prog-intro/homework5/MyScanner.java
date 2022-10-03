import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MyScanner implements AutoCloseable {
    private final Reader reader;
    private static final int BUFFER_SIZE = 1024;
    private CharBuffer buffer;

    public MyScanner(Reader reader) {
        this.reader = reader;
        this.buffer = CharBuffer.allocate(BUFFER_SIZE);
    }

    public void close() throws IOException {
        reader.close();
    }

    private boolean isBufferUpdated() throws IOException {
        //System.err.println(Arrays.toString(buffer.array()));
        if (!buffer.hasRemaining()) {
            buffer.clear();
            reader.read(buffer);
            //System.err.println("Debug: " + buffer.position());
            buffer.flip();
            //System.err.println("Debug: " + buffer.length());
           // System.err.println(buffer.position() + " " + buffer.length() + " " + Arrays.toString(buffer.array()));
            //System.err.println(buffer.hasRemaining());
            return buffer.hasRemaining();
        }
        return true;
    }

    private boolean isSeparator(char character) {
        int type = Character.getType(character);
        return type == Character.SPACE_SEPARATOR || type == Character.LINE_SEPARATOR
            || type == Character.PARAGRAPH_SEPARATOR || type == Character.CONTROL;
    }

    public boolean hasNextLine() throws IOException {
        if (!isBufferUpdated()) {
            return false;
        }
       // int n = buffer.limit() - buffer.position();
     //   System.err.println(n);
       // System.err.println(Arrays.toString(buffer.array()));
       // System.err.println(buffer.position() + " " + buffer.remaining());
        for (int position = 0; position < buffer.remaining(); position++) {
            if (Character.getType(buffer.charAt(position)) == Character.CONTROL) {
                if (position + 1 < buffer.remaining()
                && Character.getType(buffer.charAt(position + 1)) == Character.CONTROL) {
                    buffer.put(buffer.position() + position + 1, ' ');
                }
                return true;
            }
        }
        return false;
    }

    public boolean hasNext(boolean isNumber) throws IOException {
        if (!isBufferUpdated()) {
            return false;
        }

        if (isNumber) {
            for (int position = 0; position < buffer.remaining(); position++) {
                if (Character.isDigit(buffer.charAt(position))) {
                    return true;
                } else if (position == buffer.remaining() - 1 && !isBufferUpdated()) {
                    return false;
                }
            }
            return false;
        }
     
        return true;
    }

    public String nextLine() throws IOException {
        if (!isBufferUpdated()) {
            throw new IOException("The resource has ended.");
        }
        StringBuilder builder = new StringBuilder();
        while (buffer.hasRemaining() || isBufferUpdated()) {
            char character = buffer.get();
            if (Character.getType(character) != Character.CONTROL) {
                builder.append(character);
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
        return builder.toString();
    }

    public String next(boolean isNumber) throws IOException {
        if (!isBufferUpdated()) {
            throw new IOException("The resource has ended.");
        }
        StringBuilder builder = new StringBuilder();

        if (isNumber) {
            while (buffer.hasRemaining() || isBufferUpdated()) {
                char character = buffer.get();
                if (Character.isDigit(character) || (builder.isEmpty() && character == '-')) {
                    builder.append(character);
                } else if (!builder.isEmpty()) {
                    break;
                }
            }
        } else {
            while (buffer.hasRemaining() || isBufferUpdated()) {
                char character = buffer.get();
                if (!isSeparator(character)) {
                    builder.append(character);
                } else if (!builder.isEmpty()) {
                    break;
                }
            }
        }
        /*int count = 0;
        while ((buffer.hasRemaining() || isBufferUpdated())) {
            if (Character.getType(buffer.charAt(0)) == Character.CONTROL && count < 2) {
                buffer.get();
                count++;
            } else {
                break;
            }
        }*/
        return builder.toString();
    }
}
