import java.io.*;
import java.util.*;

public class ElfParser {
    private final short[] bytes;
    private final DataInputStream reader;
    private final BufferedWriter writer;
    private final List<SymTableEntity> entities;
    private final Map<String, Section> sections;
    private final Map<Integer, Integer> symTable;
    private int textAddr;

    public ElfParser(final String inputFileName, final String outputFileName) throws IOException {
        try {
            this.reader = new DataInputStream(new FileInputStream(inputFileName));
            this.writer = new BufferedWriter(new FileWriter(outputFileName));
            this.entities = new ArrayList<>();
            this.sections = new HashMap<>();
            this.symTable = new HashMap<>();
            byte[] tmp = reader.readAllBytes();
            this.reader.close();
            this.bytes = new short[tmp.length];

            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (short) (((short)((tmp[i] < 0) ? 256 : 0)) + ((short)tmp[i]));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public void parse() throws IOException {
        parseHeader();
        parseSymTable();
        parseText();
        saveSymTable();
        writer.close();
    }

    private void parseHeader() {
        final int shoff = cnt(32, 4);
        final int shentsiz = cnt(46, 2);
        final int shnum = cnt(48, 2);
        final int shstrndx = cnt(50, 2);
        final int go = shoff + shentsiz * shstrndx;
        final int offset12 = cnt(go + 16, 4);
        for (int j = 0, i = shoff; j < shnum; j++, i += 40) {
            final String name = getName(offset12 + cnt(i, 4));
            int[] params = new int[10];
            for (int k = 0; k < 10; k++) {
                params[k] = cnt(i + 4 * k, 4);
            }
            sections.put(name, new Section(
                    params[0], params[1],
                    params[2], params[3],
                    params[4], params[5],
                    params[6], params[7],
                    params[8], params[9]
            ));
        }
    }



    private void parseSymTable() {
        final Section symtab = sections.get(".symtab");
        final int offset = symtab.offset;
        final int count = symtab.size / 16;
        for (int i = 0; i < count; i++) {
            final StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                builder.append(decimalToBinary(bytes[offset  + i * 16 + 12 + j]));
            }
            final String str = builder.toString();
            final String name = getName(cnt(offset + i * 16, 4) + sections.get(".strtab").offset);
            final int value = cnt(offset + i * 16 + 4, 4);
            final int size = cnt(offset + i * 16 + 8, 4);
            final int info = Integer.parseInt(new StringBuilder(str.substring(0, 8)).reverse().toString(), 2);
            final int other = Integer.parseInt(new StringBuilder(str.substring(8)).reverse().toString(), 2);
            System.out.println(value + " " + size + " " + info + " " + other);
            symTable.put(value, entities.size());
            entities.add(new SymTableEntity(name, value, size, i, info, other));
        }
        textAddr = sections.get(".text").addr;
    }

    private void saveSymTable() throws IOException {
        writer.write(".symtab");
        writer.newLine();;
        writer.write("Symbol Value          \tSize Type \tBind \tVis   \tIndex Name");
        writer.newLine();
        for (SymTableEntity entity : entities) {
            writer.write(entity.toString());
            writer.newLine();
        }
    }

    private void parseText() {

    }

    private int cnt(int offset, int num) {
        int ans = 0;
        for (int i = num - 1; i >= 0; i--) {
            ans = ans * 256 + bytes[offset + i];
        }
        return ans;
    }

    private static String decimalToBinary(int x) {
        final StringBuilder builder = new StringBuilder(Integer.toBinaryString(x));
        while (builder.length() < 8) {
            builder.append("0");
        }
        return builder.toString();
    }

    private String getName(int offset) {
        final StringBuilder builder = new StringBuilder();
        while (bytes[offset] != 0) {
            builder.append((char)bytes[offset++]);
        }
        return builder.toString();
    }
}
