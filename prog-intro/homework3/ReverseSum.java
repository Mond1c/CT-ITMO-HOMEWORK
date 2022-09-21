import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class ReverseSum {
    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);
        int[][] matrix = new int[2][];
        int[] row_sizes = new int[2];
        int i = 0;
        while (consoleScanner.hasNextLine()) {
            Scanner stringScanner = new Scanner(consoleScanner.nextLine());
            int[] row = new int[2];
            int j = 0;
            while (stringScanner.hasNextInt()) {
                if (j == row.length) {
                    row = Arrays.copyOf(row, row.length + (row.length >> 1));
                }
                row[j++] = stringScanner.nextInt();
            }
            if (i == matrix.length) {
                matrix = Arrays.copyOf(matrix, matrix.length + (matrix.length >> 1));
                row_sizes = Arrays.copyOf(row_sizes, row_sizes.length + (row_sizes.length >> 1));
            }
            matrix[i] = row;
            row_sizes[i++] = j;
            stringScanner.close();
        }
        consoleScanner.close();
        for (i = 0; i < matrix.length; i++) {
            if (matrix[i] == null) break;
            for (int j = 0; j < row_sizes[i]; j++) {
                int sum = 0;
                for (int k = 0; k < matrix.length; k++) {
                    if (matrix[k] != null && j < row_sizes[k]) {
                        sum += matrix[k][j];
                    }
                }
                for (int m = 0; m < row_sizes[i]; m++) {
                    sum += matrix[i][m];
                }
                sum -= matrix[i][j];
                System.out.print(sum);
                if (j < row_sizes[i] - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}