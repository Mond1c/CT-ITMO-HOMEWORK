import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import RISCVDisassembler.BinaryFile;
import RISCVDisassembler.ELFHeader;
import RISCVDisassembler.ELFSymbolTable;
import RISCVDisassembler.ELFText;


public class rv3 {
    public static void main(String[] args) {
        if (args.length != 2) { 
            System.out.println("Argument count must equal to 2. Your: " + args.length);
            return;
        } else if (args[0] == args[1]) {
            System.out.println("Input and output files are identical. Please, change output file's name.");
        }
        try (PrintWriter out = new PrintWriter(new FileOutputStream(args[1]))) {
            InputStream stream = new FileInputStream(args[0]);
            BinaryFile file = new BinaryFile(stream.readAllBytes());
            stream.close();
            if (!ELFHeader.checkForTask(file)) {
                System.err.println("Sorry, input file must be ELF & 32bit & Little-Endian & RISC-V");
                return;
            }
            ELFText.output(file, out);
            out.println();
            ELFSymbolTable.output(file, out);
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Runtime exception: " + e.getMessage());
        }
        return;
    }
}