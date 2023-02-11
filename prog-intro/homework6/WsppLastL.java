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
    public static void main(String[] args) {
        try (MyScanner reader = new MyScanner(new FileReader(args[0],
                StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1],
                     StandardCharsets.UTF_8))) {
            Map<String, PairOfIntegerAndIntList> dict = new LinkedHashMap<>();
            int index = 1;
            while (reader.hasNextLine()) {
                MyScanner stringScanner = new MyScanner(reader.nextLine(), true);
                Map<String, PairOfIntegers> lastIndexes = new LinkedHashMap<>();
                while (stringScanner.hasNextWord()) {
                    String word = stringScanner.nextWord().toLowerCase();
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

