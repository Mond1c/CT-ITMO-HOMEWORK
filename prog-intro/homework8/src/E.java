import java.util.Scanner;

public class E {

    private static int dfs(boolean[][] vertices, int start, int end, boolean[] visited, int depth) {
        if (start == end) {
            return depth;
        }
        visited[start] = true;
        for (int vertex : vertices[]) {
            if (!visited[vertex]) {
                int a = dfs(vertices, vertex, end, visisted, depth+1);
                if (a != -1) {
                    return a;
                }
            }
        }
        return -1;
    }

    private static int findTeamDepth(boolean[][] vertices, int start, int team) {
        
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        boolean[][] vertices = new boolean[n][n];
        for (int i = 0; i < n - 1; i++) {
            vertices[scanner.nextInt() - 1][scanner.nextInt() - 1] = true;
        }
        int[] c = new int[m];
        for (int i = 0; i < m; i++) {
            c[i] = scanner.nextInt();
        }

        
    }
}
