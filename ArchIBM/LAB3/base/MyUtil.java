package base;

public class MyUtil {
    private MyUtil() {} 

    public static int BytesToInt(byte[] data) {
        int result = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            result = (result << 8) + ((256 + data[i]) % 256);
        }
        return result;
    }

    public static byte[] IntToBytes(int number, int size) {
        byte[] res = new byte[size];
        for (int i = 0; i < number; i++) {
            res[i] = (byte)(number % 256);
            number /= 256;
        }
        return res;
    }
}
