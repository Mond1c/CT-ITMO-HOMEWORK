package md2html;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("You need to indicate input file and output file in program args");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0], StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[1], StandardCharsets.UTF_8))) {
            Document document = Document.markdownToDocument(reader);
            document.writeHtmlIntoFile(writer);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Can't read from the file or write to the file.");
        }
    }
}
