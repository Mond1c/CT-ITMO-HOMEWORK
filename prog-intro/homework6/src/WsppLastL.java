import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class PairOfIntegers {
    public int first;
    public int second;

    public PairOfIntegers(int first, int second) {
        this.first = first;
        this.second = second;
    }
}

class PairOfIntegerAndIntList {
    public int first;
    public IntList second;

    public PairOfIntegerAndIntList(int first, IntList second) {
        this.first = first;
        this.second = second;
    }
}

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
        try (MyScanner reader = new MyScanner(new FileReader(args[0],
                StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1],
                     StandardCharsets.UTF_8))) {
            Map<String, PairOfIntegerAndIntList> dict = new LinkedHashMap<>();
            int index = 1;
            while (reader.hasNextLine()) {
                List<String> words = getWords(reader.nextLine());
                Map<String, PairOfIntegers> lastIndexes = new LinkedHashMap<>();
                for (String word : words) {
                    word = word.toLowerCase();
                    PairOfIntegers pair = lastIndexes.getOrDefault(word, new PairOfIntegers(0, 0));
                    pair.first++;
                    pair.second = index++;
                    lastIndexes.put(word, pair);
                }
                for (Map.Entry<String, PairOfIntegers> item : lastIndexes.entrySet()) {
                    String key = item.getKey();
                    PairOfIntegerAndIntList pair = dict.getOrDefault(key, new PairOfIntegerAndIntList(0, new IntList()));
                    pair.second.add(item.getValue().second);
                    pair.first += item.getValue().first;
                    dict.put(key, pair);
                }
                index = 1;
            }
            for (Map.Entry<String, PairOfIntegerAndIntList> item : dict.entrySet()) {
                writer.write(item.getKey() + " " + item.getValue().first);
                IntList list = item.getValue().second;
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

