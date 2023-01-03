#include <bits/stdc++.h>

int main(int argc, char const *argv[])
{
	std::cin.tie(nullptr);
	std::ios::sync_with_stdio(false);
	int n;
	std::cin >> n;
	std::vector<int> a(n);
	std::string str;
	std::cin >> str;
	for (int i = 0; i < str.size(); ++i) {
		if (str[i] == 'w') {
			a[i] = std::numeric_limits<int>::min();
		} else if (str[i] == '"') {
			a[i] = 1;
		}
	}
	std::vector<int> dp(n, -1);
	dp[0] = a[0];
	for (int i = 1; i < dp.size(); ++i) {
		int max = dp[i - 1];
		if (i - 3 >= 0) max = std::max(dp[i - 3], max);
		if (i - 5 >= 0) max = std::max(dp[i - 5], max);
		if (max >= 0) dp[i] = max + a[i];
	}
	std::cout << (dp[n - 1] < 0 ? -1 : dp[n - 1]) << std::endl;
	return 0;
}