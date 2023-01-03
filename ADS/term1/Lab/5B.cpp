#include <bits/stdc++.h>

int main() {
	int n, m;
	std::cin >> n >> m;
	std::vector<std::vector<int>> a(n, std::vector<int>(m));
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < m; ++j) {
			std::cin >> a[i][j];
		}
	}
	std::vector<std::vector<std::pair<int, char>>> dp(n, std::vector<std::pair<int, char>>(m));
	dp[0][0] = {a[0][0], '0'};
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < m; ++j) {
			if (i == 0 && j == 0) continue;
			if (i - 1 >= 0) {
				dp[i][j] = {dp[i - 1][j].first + a[i][j], 'D'};
			}
			if (j - 1 >= 0 && (dp[i][j - 1].first + a[i][j] >= dp[i][j].first || i - 1 < 0)) {
				dp[i][j] = {dp[i][j - 1].first + a[i][j], 'R'};
			}
		}
	}
	/*
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < m; j++) {
			std::cout << "{" << dp[i][j].first << "," << dp[i][j].second << "}" << ";";
		}
		std::cout << std::endl;
	} */
	std::string ans;
	int i = n - 1, j = m - 1;
	while (dp[i][j].second != '0') {
		ans += dp[i][j].second;
		if (dp[i][j].second == 'D') {
			i--;
		} else {
			j--;
		}
	}
	std::reverse(ans.begin(), ans.end());
	std::cout << dp[n - 1][m - 1].first << std::endl << ans << std::endl;
	return 0;
}