import java.util.Scanner;

public class Three {
    public static void main(String[] args) {
        final int n = new Scanner(System.in).nextInt();
        for (int i = 0; i < Math.pow(3, n - 1); i++) {
            final StringBuilder code = new StringBuilder(Integer.toBinaryString(i ^ (i / 2)));
            while (code.length() < n) {
                code.insert(0, '0');
            }
            System.out.println(code);
            final StringBuilder revCode = new    StringBuilder();
            for (char ch : code.toString().toCharArray()) {
                if (ch == '0') {
                    revCode.append('1');
                } else {
                    revCode.append('0');
                }
            }
            System.out.println(revCode);
        }
    }
}
