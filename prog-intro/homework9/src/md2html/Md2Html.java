package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) {
        try {
            Parser parser = new Parser(new BufferedReader(new FileReader(args[0], StandardCharsets.UTF_8)),
                    new BufferedWriter(new FileWriter(args[1], StandardCharsets.UTF_8)));
            parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
