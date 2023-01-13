#include <bits/stdc++.h>
#define ull unsigned long long

ull solve(std::vector<int> &a, int i, int k, ull count) {
    if (i == a.size()) {
        if (k <= 0) {
            count++;
        }
        return count;
    }
    count = solve(a, i + 1, k - a[i], count);
    count = solve(a, i + 1, k, count);
    return count;
}

int main() {
    int n, k;
    std::cin >> n >> k;
    std::vector<int> a(n);
    for (int i = 0; i < n; i++) {
        std::cin >> a[i];
    }
    std::cout << solve(a, 0, k, 0) << std::endl;
    return 0;
}