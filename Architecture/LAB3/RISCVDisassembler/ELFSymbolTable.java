package RISCVDisassembler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ELFSymbolTable {
    public static void output(BinaryFile file, PrintWriter out) {
        ELFSectionHeaderEntry symbolTableAsEntry = ELFSectionHeader.getSection(file, ".symtab");
        ELFSectionHeaderEntry stringTable = ELFSectionHeader.getSection(file, ELFSymbolTable.getStringTableNdx(file, symbolTableAsEntry));


        out.format(".symtab\n");
        out.format("Symbol Value          	  Size Type 	Bind 	 Vis   	    Index Name\n");

        for (int i = 0; i < symbolTableAsEntry.getSize(file) / symbolTableAsEntry.getEntrySize(file); i++) {
            int address = symbolTableAsEntry.getOffset(file) + i * symbolTableAsEntry.getEntrySize(file);
            out.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s\n",
                i, 
                ELFSymbolTableEntry.getValue(file, address),        
                ELFSymbolTableEntry.getSize(file, address),
                ELFSymbolTableEntry.getType(file, address),
                ELFSymbolTableEntry.getBind(file, address),       
                ELFSymbolTableEntry.getVisibility(file, address),
                ELFSymbolTableEntry.getSectionObjectBelongsTo(file, address),
                ELFSymbolTableEntry.getName(file, address, stringTable)
            );
        }
    }

    public static int getStringTableNdx(BinaryFile file, ELFSectionHeaderEntry symbolTableAsEntry) {
        return symbolTableAsEntry.getLink(file);
    }

    public static Map<Integer, String> getMap(BinaryFile file) {
        ELFSectionHeaderEntry symbolTableAsEntry = ELFSectionHeader.getSection(file, ".symtab");
        ELFSectionHeaderEntry stringTable = ELFSectionHeader.getSection(file, ELFSymbolTable.getStringTableNdx(file, symbolTableAsEntry));

        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < symbolTableAsEntry.getSize(file) / symbolTableAsEntry.getEntrySize(file); i++) {
            int address = symbolTableAsEntry.getOffset(file) + i * symbolTableAsEntry.getEntrySize(file);
            if (ELFSymbolTableEntry.getTypeType(file, address) == 2) // Function
            result.put( 
                        ELFSymbolTableEntry.getValue(file, address),
                        ELFSymbolTableEntry.getName(file, address, stringTable)
                        );
        }

        return result;
    }
}

class ELFSymbolTableEntry {
    public static String getName(BinaryFile file, int address, ELFSectionHeaderEntry stringTable) {
        if (getTypeType(file, address) == 0x03) {           // if it's section => give its own name
            return getSection(file, address).getName(file);
        } else {
            int start = stringTable.getOffset(file) +  // space of names
                file.getValue(address, 4);         // sh_name - offset in space of names
            int end = start;
            while ((char) file.getByte(end) != '\0') { ++end; }
            return new String(file.getByteArray(start, end), 0, end - start);
        }
    }

    public static int getValue(BinaryFile file, int address) {
        return file.getValue(address + 4, 4);
    }

    public static int getSize(BinaryFile file, int address) {
        return file.getValue(address + 8, 4);
    }

    public static int getBindType(BinaryFile file, int address) {
        return file.getValue(address + 12, 1) >> 4;
    }

    public static String getBind(BinaryFile file, int address) {
        return switch (getBindType(file, address)) {
            case 0 -> "LOCAL";
            case 1 -> "GLOBAL";
            case 2 -> "WEAK";
            case 10 -> "LOOS";
            case 12 -> "HIOS";
            case 13 -> "LOPROC";
            case 15 -> "HIPROC";
            default -> "ERROR";
        };
    }

    public static int getTypeType(BinaryFile file, int address) {
        return file.getValue(address + 12, 1) & (0xf); 
    }

    public static String getType(BinaryFile file, int address) {
        return switch (getTypeType(file, address)) {
            case 0 -> "NOTYPE";  
            case 1 -> "OBJECT";  
            case 2 -> "FUNC";
            case 3 -> "SECTION";
            case 4 -> "FILE";
            default -> String.format("%x ERROR", getTypeType(file, address));
        };
    }

    public static int getVisibilityType(BinaryFile file, int address) {
        return file.getValue(address + 13, 1);
    }

    public static String getVisibility(BinaryFile file, int address) {
        return switch(getVisibilityType(file, address)) {
            case 0 -> "DEFAULT";
            case 1 -> "INTERNAL";
            case 2 -> "HIDDEN";
            case 3 -> "PROTECTED";
            default -> "ERROR";
        };
    }

    public static ELFSectionHeaderEntry getSection(BinaryFile file, int address) {
        return ELFSectionHeader.getSection(file, file.getValue(address + 14, 2));
    }

    public static String getSectionObjectBelongsTo(BinaryFile file, int address) {
        int shndx = file.getValue(address + 14, 2);
        return switch (shndx) {
            case 0 -> String.valueOf("UNDEF");
            case 0xfff1 -> String.valueOf("ABS");
            default -> String.valueOf(shndx);
        };
    }

}
