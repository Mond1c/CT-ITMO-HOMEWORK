import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wspp {
    public static void main(String[] args) {
        try (MyScanner reader = new MyScanner(new InputStreamReader(new FileInputStream(args[0]),
                StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]),
                     StandardCharsets.UTF_8))) {
            Map<String, IntList> dict = new LinkedHashMap<>();
            int index = 1;
            while (reader.hasNextLine()) {
                MyScanner stringScanner = new MyScanner(reader.nextLine(), true);
                while (stringScanner.hasNextWord()) {
                    String word = stringScanner.nextWord().toLowerCase();
                    IntList value = dict.getOrDefault(word, new IntList());
                    value.add(index++);
                    dict.put(word, value);
                }
            }
            for (Map.Entry<String, IntList> item : dict.entrySet()) {
                writer.write(item.getKey() + " " + item.getValue().size());
                for (int i = 0; i < item.getValue().size(); i++) {
                    writer.write(" " + item.getValue().get(i));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}