package expression.exceptions;

public class ParserException extends Exception {
    public ParserException(String expected, String found, int pos) {
        super(String.format("Expected '%s', found '%s' at %d", expected, found == "" ? "END" : found, pos)); 
    }

    public ParserException(String msg, int pos) {
        super(String.format("%s at %d", msg, pos)); 
    }

    public ParserException(String msg) {
        super(msg); 
    }
}
