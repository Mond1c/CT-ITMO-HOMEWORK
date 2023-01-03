import java.util.Scanner;

public class Third {
    private static String getThirdVector(int x, int n) {
        final StringBuilder builder = new StringBuilder();
        while (x > 0) {
            builder.append(n % 3);
            x /= 3;
        }
        while (builder.length() < n) {
            builder.insert(0, '0');
        }
        return builder.toString();
    }

    private static String getNewString(final String str, int n) {
        final StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {
            builder.append((Character.digit(ch, 10) + n) % 3);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        final int n = new Scanner(System.in).nextInt();
        for (int i = 0; i < Math.pow(3, n - 1); i++) {
            final String vector = getThirdVector(i, n);
            for (int j = 0; j < 3; j++) {
                System.out.println(getNewString(vector, j));
            }
        }
    }
}
