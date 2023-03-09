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

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    continue;
                }
                matrix[i][j] = 1;
                for (int k = j + 1; k < n; k++) {
                    if (matrix[i][k] >= matrix[j][k]) {
                        matrix[i][k] = (matrix[i][k] - matrix[j][k]) % 10;
                    } else {
                        matrix[i][k] = 0;
                    }
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
