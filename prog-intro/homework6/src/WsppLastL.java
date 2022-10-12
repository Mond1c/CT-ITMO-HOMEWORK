import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WsppLastL {


    private static List<String> getWords(String line) {
        List<String> words = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            boolean isPartOfWord = Character.isLetter(character) || character == '\'' ||
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
        try (Scanner reader = new Scanner(new FileReader(args[0],
                StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1],
                     StandardCharsets.UTF_8))) {
            Map<String, Pair> dict = new LinkedHashMap<>();
            int index = 1;
            while (reader.hasNextLine()) {
                List<String> words = getWords(reader.nextLine());
                Map<String, IntList> lastIndexes = new LinkedHashMap<>();
                for (String word : words) {
                    word = word.toLowerCase();
                    if (!lastIndexes.containsKey(word)) {
                        lastIndexes.put(word, new IntList());
                    }
                    lastIndexes.get(word).add(index++);
                }
                for (Map.Entry<String, IntList> item : lastIndexes.entrySet()) {
                    String key = item.getKey();
                    if (!dict.containsKey(key)) {
                        dict.put(key, new Pair(0, new IntList()));
                    }
                    IntList value = lastIndexes.get(key);
                    dict.get(key).getList().add(value.get(value.size() - 1));
                    dict.get(key).addSize(value.size());
                }
                index = 1;
            }
            for (Map.Entry<String, Pair> item : dict.entrySet()) {
                writer.write(item.getKey() + " " + item.getValue().getSize());
                IntList list = item.getValue().getList();
                for (int i = 0 ; i < list.size(); i++) {
                    writer.write(" " + list.get(i));
                }

                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Pair {
    private int size;
    private IntList list;

    public Pair(int size, IntList list) {
        this.size = size;
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void addSize(int size) {
        this.size += size;
    }

    public IntList getList() {
        return list;
    }
}