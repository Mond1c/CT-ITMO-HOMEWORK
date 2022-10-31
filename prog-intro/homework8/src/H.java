import java.util.Scanner;

public class H {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int q = scanner.nextInt();
        int[] t = new int[q];
        for (int i = 0; i < q; i++) {
            t[i] = scanner.nextInt();
        }
    }
}