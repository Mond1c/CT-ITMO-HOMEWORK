#include <bits/stdc++.h>


int main() {
	int n;
	std::cin >> n;
	std::vector<int> a(n);
	for (int i = 0; i < n; ++i) std::cin >> a[i];
	int m;
	std::cin >> m;
	std::vector<int> b(m);
	for (int i = 0; i < m; ++i) std::cin >> b[i];
	std::vector<std::vector<int>> dp(n + 1, std::vector<int>(m + 1));
	std::vector<std::vector<std::pair<int, int>>> prev(n + 1, std::vector<std::pair<int, int>>(m + 1));
	for (int i = 1; i <= n; ++i) {
		for (int j = 1; j <= m; ++j) {
			if (a[i - 1] == b[j - 1]) {
				dp[i][j] = dp[i - 1][j - 1] + 1;
				prev[i][j] = {i - 1, j - 1};
			} else {
				if (dp[i - 1][j] >= dp[i][j - 1]) {
					dp[i][j] = dp[i - 1][j];
					prev[i][j] = {i - 1, j};
				} else {
					dp[i][j] = dp[i][j - 1];
					prev[i][j] = {i, j - 1};
				}
			}
		}
	}/*
	for (int i = 0; i <= n; ++i) {
		for (int j = 0; j <= m; ++j) {
			std::cout << dp[i][j] << " ";
		}
		std::cout << std::endl;
	}
		for (int i = 0; i <= n; ++i) {
		for (int j = 0; j <= m; ++j) {
			std::cout << "{" << prev[i][j].first << "," << prev[i][j].second << "}" << ";";
		}
		std::cout << std::endl;
	}*/
	std::cout << dp[n][m] << std::endl;
	int i = n, j = m;
	std::vector<int> ans;
	while (i != 0 && j != 0) {
		if (prev[i][j] == std::pair<int, int>{i - 1, j - 1}) {
			//std::cout << i << " " << j << std::endl;
			ans.push_back(a[i - 1]);
			--i; --j;
		} else {
			if (prev[i][j] == std::pair<int, int>{i - 1, j}) {
				i--;
			} else {
				j--;
			}
		}
	}
	std::reverse(ans.begin(), ans.end());
	for (int v : ans) std::cout << v << " ";
	std::cout << std::endl;
	return 0;
}