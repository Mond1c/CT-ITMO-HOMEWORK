import java.math.BigInteger;
import java.util.Scanner;

public class G5 {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String s = scanner.next();
        final int n = scanner.nextInt();

        BigInteger[] dp = new BigInteger[n];
        dp[s.length() - 1] = BigInteger.valueOf(1);
        dp[n - 1] = BigInteger.valueOf(25).pow(n - s.length());
        System.out.println(dp[n - 1].mod(BigInteger.valueOf(998244353)));
    }
}
