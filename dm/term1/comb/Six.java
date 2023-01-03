import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Six {
    private static String getBinaryString(int n, int x) {
        final StringBuilder builder = new StringBuilder(Integer.toBinaryString(x));
        while (builder.length() < n) {
            builder.insert(0, '0');
        }
        return builder.toString();
    }

    private static boolean isValid(final String str) {
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == '1' && str.charAt(i - 1) == '1') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        final int n = new Scanner(System.in).nextInt();
        List<String> vectors = new ArrayList<>();
        for (int i = 0; i < Math.pow(2, n); i++) {
            final String code = getBinaryString(n, i);
            if (isValid(code)) {
                vectors.add(code);
            }
        }
        System.out.println(vectors.size());
        for (final String code : vectors) {
            System.out.println(code);
        }
    }
}