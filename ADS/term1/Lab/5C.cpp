#include <bits/stdc++.h>

int main() {
	int n;
	std::cin >> n;
	std::vector<int> a(n);
	for (int i = 0; i < n; i++) std::cin >> a[i];
	std::vector<int> dp(n, 1);
	std::vector<int> prev(n, -1);
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < i; ++j) {
			if (a[j] < a[i] && dp[j] + 1 > dp[i]) {
				dp[i] = dp[j] + 1;
				prev[i] = j;
			}
		}
	}
	int pos = 0;
	int len = dp[0];
	for (int i = 0; i < n; i++) {
		if (dp[i] > len) {
			pos = i;
			len = dp[i];
		}
	}
	std::vector<int> ans;
	while (pos != -1) {
		ans.push_back(a[pos]);
		pos = prev[pos];
	}
	std::reverse(ans.begin(), ans.end());
	std::cout << len << std::endl;
	for (int v : ans) std::cout << v << " ";
	std::cout << std::endl;
		
	return 0;
}