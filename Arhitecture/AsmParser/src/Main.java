import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new ElfParser("test_elf.elf", "output.txt").parse();
    }
}
