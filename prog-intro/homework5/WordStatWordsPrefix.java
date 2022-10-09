import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatWordsPrefix {
    private static List<String> getWords(String line) {
        List<String> words = new LinkedList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            boolean isPartOfWord = Character.isLetter(character) || character == '-' || character == '\'' ||
                Character.getType(character) == Character.DASH_PUNCTUATION;
            if (isPartOfWord) { 
                word.append(Character.toLowerCase(character));
            }
            if ((!isPartOfWord || i == line.length() - 1) && !word.isEmpty()) {
                words.add(word.toString());
                word = new StringBuilder();
            }
        }
        return words;
    }

    public static void main(String[] args) {
        try (MyScanner reader = new MyScanner(new InputStreamReader(new FileInputStream(args[0]),
                    StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]),
                    StandardCharsets.UTF_8))) {
            Map<String, Integer> dict = new TreeMap<>();
            String line = reader.nextLine();
            while (line != null) {
                List<String> words = getWords(line);
                for (String word : words) {
                    if (!word.isEmpty()) {
                        String prefix = word.substring(0, Math.min(3, word.length()));
                        dict.put(prefix, dict.getOrDefault(prefix, 0) + 1);
                    }
                }
                line = reader.nextLine();
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