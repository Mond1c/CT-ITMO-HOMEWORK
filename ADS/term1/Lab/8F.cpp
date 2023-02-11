#include <bits/stdc++.h>

int main() {
	int n, s;
	std::cin >> n >> s;
	std::vector<int> l(n), r(n);
	for (int i = 0; i < n; ++i) {
		std::cin >> l[i] >> r[i]; 
	}
	std::vector<std::vector<int>> dp(n, std::vector<int>(s));
	return 0;
}