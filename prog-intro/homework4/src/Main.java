import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static List<String> getWords(String line) {
        List<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            boolean isPartOfWord = Character.isLetter(ch) || ch == '\'' || ch == '-';
            if (isPartOfWord) {
                word.append(Character.toLowerCase(ch));
            }
            if (ch == ' ' || i == line.length() - 1) {
                if (!word.isEmpty()) words.add(word.toString());
                word = new StringBuilder();
            }
        }
        return words;
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt"), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), StandardCharsets.UTF_8))) {
            Map<String, Integer> dict = new LinkedHashMap<>();
            String line = reader.readLine();
            while (line != null) {
                List<String> words = getWords(line);
                for (String word : words) {
                    dict.put(word, dict.getOrDefault(word, 0) + 1);
                }
                line = reader.readLine();
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