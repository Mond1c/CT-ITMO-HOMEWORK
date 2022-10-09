import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wspp {
    private static List<String> getWords(String line) {
        List<String> words = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            boolean isPartOfWord = Character.isLetter(character) || character == '-' || character == '\'' ||
                    Character.getType(character) == Character.DASH_PUNCTUATION;
            if (isPartOfWord) {
                end++;
            }
            if (!isPartOfWord || i == line.length() - 1) {
                if (start != end) {
                    words.add(line.substring(start, end));
                }
                start = ++end;
            }
        }
        return words;
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]),
                StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]),
                     StandardCharsets.UTF_8))) {
            Map<String, IntList> dict = new LinkedHashMap<>();
            String line = reader.readLine();
            int index = 1;
            while (line != null) {
                List<String> words = getWords(line);
                for (String word : words) {
                    word = word.toLowerCase();
                    if (!dict.containsKey(word)) {
                        dict.put(word, new IntList());
                    }
                    dict.get(word).add(index++);
                }
                line = reader.readLine();
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