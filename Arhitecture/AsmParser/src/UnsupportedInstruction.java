public class UnsupportedInstruction extends RuntimeException {
    public UnsupportedInstruction() {

    }

    public UnsupportedInstruction(final String message) {
        super(message);
    }
}
