#include <bits/stdc++.h>

int main() {
	int n;
	int k = 0;
	std::cin >> n;
	std::vector<int> m(n);
	for (int i = 0; i < n; ++i) {
		std::cin >> m[i];
		k += m[i];
	}
	if (k % 2 == 1) {
		std::cout << -1 << std::endl;
		return 0;
	}
	std::vector<std::vector<bool>> dp(k / 2 + 1, std::vector<bool>(n));
	for (int i = 0; i < n; ++i) {
		if (a[i] <= k / 2 + 1) {
			dp[a[i]][i] = true;
		}
	}
	for (int s = 0; s <= k; ++s) {
		for (int i = 1; i < n; ++i) {
			if (!dp[s][i]) {
				dp[s][i] = dp[s][i - 1];
				if (s - a[i] >= 0) {
					dp[s][i] |= dp[s - a[i]][i];
				}
			}
		}
	}
	
	return 0;
}