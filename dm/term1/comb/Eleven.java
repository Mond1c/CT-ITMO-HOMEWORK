import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

public class Eleven {
    private final static List<List<Integer>> SUBSETS = new ArrayList<>();


	private static void getSubsets(int[] a, List<Integer> subset, int index) {
		if (index == a.length) {
			SUBSETS.add(subset);
			return;
		}
		getSubsets(a, new ArrayList<>(subset), index + 1);
		subset.add(a[index]);
		getSubsets(a, new ArrayList<>(subset), index + 1);
	}
	
	public static void main(String[] args) {
		final int n = new Scanner(System.in).nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = i + 1;
		}
		getSubsets(a, new ArrayList<>(), 0);
        Collections.sort(SUBSETS, (o1, o2) -> {
            final int m = Math.min(o1.size(), o2.size());
            for (int i = 0; i < m; i++) {
                if (o1.get(i) == o2.get(i)) {
                    continue;
                } else {
                    return o1.get(i) - o2.get(i);
                }
            }
            return o1.size() - o2.size();
        });
        for (List<Integer> subset : SUBSETS) {
            for (int v : subset) {
                System.out.printf("%d ", v);
            }
            System.out.println();
        }
	}
}

