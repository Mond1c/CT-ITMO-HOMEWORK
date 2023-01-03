import java.util.*;

public class Eight {
    private static void generateChooses(int next, int dep, int n, int k, List<Integer> a, List<List<Integer>> ans) {
        if (dep == k) {
            ans.add(a);
            return;
        }
        for (int i = k + 1; i < n; i++) {
            final List<Integer> newA = new ArrayList<>(a);
            newA.add(i);
            generateChooses(i, dep + 1, n, k, newA, ans);
        }
    }   

    private static List<int[]> generateCombinations(int n, int k) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[k];
        for (int i = 0; i < k; i++) {
            combination[i] = i;
        }

        while (combination[k - 1] < n) {
            combinations.add(combination.clone());
            int m = k - 1;
            while (m != 0 && combination[m] == n - k + m) {
                m--;
            }
            combination[m]++;
            for (int i = m + 1; i < k; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }
        return combinations;
    }


    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        final int k = scanner.nextInt();
        final int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
        }
        final List<int[]> ans = generateCombinations(n, k);
        for (int i = 0; i < ans.size(); i++) {
            for (int j = 0; j < k; j++) {
                System.out.printf("%d ", ans.get(i)[j] + 1);
            }
            System.out.println();
        }
    }
}
