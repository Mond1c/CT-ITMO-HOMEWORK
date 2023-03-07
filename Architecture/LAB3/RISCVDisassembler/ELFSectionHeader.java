package RISCVDisassembler;

public class ELFSectionHeader {
    public static ELFSectionHeaderEntry getSection(BinaryFile file, String name) {
        for (int i = 0; i < ELFHeader.getSectionHeaderEntriesCount(file); i++) {
            int address = ELFHeader.getSectionHeaderOffset(file) + 
                            i * ELFHeader.getSectionHeaderEntrySize(file);
            ELFSectionHeaderEntry curSection = new ELFSectionHeaderEntry(address);
            if (curSection.getName(file).equals(name)) {
                return curSection;
            }
        }

        throw new RuntimeException("Section " + name + " wasn't found");
    }

    public static ELFSectionHeaderEntry getSection(BinaryFile file, int ndx) {
        if (ELFHeader.getSectionHeaderEntriesCount(file) < ndx) {
            throw new RuntimeException(String.format("Given section's index (%i) is bound out: %i", ndx, 
                ELFHeader.getSectionHeaderEntriesCount(file)));
        } else {
            return new ELFSectionHeaderEntry(ELFHeader.getSectionHeaderOffset(file) + 
                                ndx * ELFHeader.getSectionHeaderEntrySize(file));
        }
    }
}

class ELFSectionHeaderEntry {
    int address;

    public ELFSectionHeaderEntry(int address) {
        this.address = address;
    }

    public int getOffset(BinaryFile file) {
        return file.getValue(address + 0x10, 4);
    }

    public String getName(BinaryFile file) {
        ELFSectionHeaderEntry ShStr = new ELFSectionHeaderEntry(ELFHeader.getSectionHeaderOffset(file) 
                + ELFHeader.getSectionHeaderEntrySize(file) * ELFHeader.getShStrNdx(file));
        int start = ShStr.getOffset(file) +      // space of names
            file.getValue(address, 4);      // sh_name - offset in space of names
        int end = start;
        while ((char) file.getByte(end) != '\0') { ++end; }
        return new String(file.getByteArray(start, end), 0, end - start);
    }

    public int getLink(BinaryFile file) {
        return file.getValue(address + 0x18, 4);
    }

    public int getInfo(BinaryFile file) {
        return file.getValue(address + 0x1C, 4);
    }

    public int getEntrySize(BinaryFile file) {
        return file.getValue(address + 0x24, 4);
    }

    public int getSize(BinaryFile file) {
        return file.getValue(address + 0x14, 4);
    }

    public int getAddress(BinaryFile file) {
        return file.getValue(address + 0xc, 4);
    }
}