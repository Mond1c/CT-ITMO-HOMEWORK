import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatInput {
    public static void main(String[] args) {
        try (MyScanner reader = new MyScanner(new InputStreamReader(new FileInputStream(args[0]),
                    StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]),
                    StandardCharsets.UTF_8))) {
            Map<String, Integer> dict = new LinkedHashMap<>();
            while (reader.hasNextLine()) {
                List<String> words = new ArrayList<>();
                MyScanner stringScanner = new MyScanner(reader.nextLine(), true);
                while (stringScanner.hasNextWord()) {
                    words.add(stringScanner.nextWord().toLowerCase());
                }
                for (String word : words) {
                    if (!word.isEmpty()) {
                        dict.put(word, dict.getOrDefault(word, 0) + 1);
                    }
                }
            }
            for (Map.Entry<String, Integer> item : dict.entrySet()) {
                writer.write(item.getKey() + " " + item.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
