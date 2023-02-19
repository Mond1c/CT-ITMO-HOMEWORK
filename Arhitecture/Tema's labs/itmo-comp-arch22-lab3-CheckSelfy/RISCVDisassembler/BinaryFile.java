package RISCVDisassembler;

import java.util.Arrays;

public class BinaryFile {
    private byte[] data;

    public BinaryFile(byte[] data) {
        this.data = data;
    }

    public byte getByte(int idx) {
        return data[idx];
    }

    public byte[] getByteArray(int idx, int len) {
        return Arrays.copyOfRange(data, idx, idx + len);
    }

    public int getValue(int idx, int len) {
        assert len <= 4: "len must be <= 4 cause int is 32bit";
        return base.MyUtil.BytesToInt(getByteArray(idx, len));
    }

}
