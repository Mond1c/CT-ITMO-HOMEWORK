package md2html;


import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("You need more two arguments");
        }
        Parser parser = new Parser(new BufferedReader(new FileReader(args[0], StandardCharsets.UTF_8)),
                new BufferedWriter(new FileWriter(args[1], StandardCharsets.UTF_8)));
        parser.parse();
    }
}
