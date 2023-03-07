package RISCVDisassembler;

import java.util.Arrays;

public class ELFHeader {
    public static boolean checkForTask(BinaryFile file) {
        return Arrays.equals(
                    file.getByteArray(0, 4),
                    new byte[]{0x7f, 0x45, 0x4c, 0x46})  && // checking for magic
            file.getValue(4, 1) == 1            && // 32-bit system 
            file.getValue(5, 1) == 1            && // little endianness
            file.getValue(7, 1) == 0 && // System-V
            file.getValue(6, 1) == 1 && // ELF Version 
            file.getValue(0x10, 2) == 2 && // executable
            file.getValue(0x14, 4) == 1 && // ELF Version
            Arrays.equals(
                    file.getByteArray(0x12, 2),    // RISC-V
                    new byte[]{(byte) 0xf3, 0x00});
    }

    public static int getShStrNdx(BinaryFile file) {
        return file.getValue(0x32, 2);
    }

    public static int getSectionHeaderOffset(BinaryFile file) {
        return file.getValue(0x20, 4);
    }

    public static int getSectionHeaderEntrySize(BinaryFile file) {
        return file.getValue(0x2E, 2);
    }

    public static int getSectionHeaderEntriesCount(BinaryFile file) {
        return file.getValue(0x30, 2);
    }
}
