import java.util.Scanner;

public class J {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            String line = scanner.next();
            for (int j = 0; j < n; j++) {
                matrix[i][j] = line.charAt(j) - '0';
            }
        }
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                if (matrix[j][k] == 0) {
                    continue;
                }
                matrix[j][k] = 1;
                for (int i = k + 1; i < n; i++) {
                    matrix[j][i] = Math.abs(matrix[j][i] - matrix[k][i]) % 10;
                }                
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
}
