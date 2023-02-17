#include <bits/stdc++.h>

int main() {
    int n;
    std::cin >> n;
    std::vector<int> a(n);
    for (int i = 0; i < n; ++i) {
        std::cin >> a[i];
    }
    std::vector<std::vector<int>> dp(n, std::vector<int>(5, -1));
    for (int i = 0; i < 5; ++i) {
        dp[0][i] = i;
    }
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < 5; ++j) {
            if (a[i - 1] < a[i] && dp[i - 1])
        }
    }
    return 0;
}