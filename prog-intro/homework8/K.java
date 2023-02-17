import java.util.Scanner;

public class K {
    private static void printMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), m = scanner.nextInt();
        char[][] matrix = new char[n][m];
        for (int i = 0; i < n; i++) {
            String row = scanner.next();
            for (int j = 0; j < m; j++) {
                matrix[i][j] = row.charAt(j);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] != '.') {
                    int row = i + 1;
                    while (row < n && matrix[row][j] == '.') {
                        matrix[row][j] = matrix[row++ - 1][j];
                    }
                    row = i - 1;
                    while (row >= 0 && matrix[row][j] == '.') {
                        matrix[row][j]  = matrix[row-- + 1][j];
                    }
                }
            }
        }
        printMatrix(matrix);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] != '.') {
                    int col = j + 1;
                    while (col < m && matrix[i][col] == '.') {
                        matrix[i][col] = matrix[i][col++ - 1];
                    }
                    col = j - 1;
                    while (col >= 0 && matrix[i][col] == '.') {
                        matrix[i][col] = matrix[i][col-- + 1];
                    }
                }
            }
        }

        printMatrix(matrix);
    }
}

