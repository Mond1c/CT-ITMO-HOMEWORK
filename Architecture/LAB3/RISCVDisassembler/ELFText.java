package RISCVDisassembler;

import java.io.PrintWriter;
import java.util.Map;

public class ELFText {
    public static void output(BinaryFile file, PrintWriter out) {
        ELFSectionHeaderEntry dotText = ELFSectionHeader.getSection(file, ".text");
        Map<Integer, String> symtab = ELFSymbolTable.getMap(file);
        int startToRead = dotText.getOffset(file),
            endToRead   = startToRead + dotText.getSize(file),
            countOfCMD  = (endToRead - startToRead) / 4;

        for (int i = 0; i < countOfCMD; i++) {
            int curCMD = dotText.getOffset(file) + i * 4; 
            int curAddress = dotText.getAddress(file) + i * 4;
            new RISCVCommand(file.getValue(curCMD, 4), curAddress).updateLabels(symtab);
        }

        out.println(".text");

        for (int i = 0; i < countOfCMD; i++) {
            int curCMD = dotText.getOffset(file) + i * 4; 
            int curAddress = dotText.getAddress(file) + i * 4;
            if (symtab.containsKey(curAddress)) {
                out.format("%08x   <%s>:\n", curAddress, symtab.get(curAddress));
            }
            RISCVCommand cmd = new RISCVCommand(file.getValue(curCMD, 4), curAddress);
            out.print(cmd.disAssembly(symtab));
        }
    }
}