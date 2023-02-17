#include <bits/stdc++.h>

int main() {
    int n, k;
    std::cin >> n >> k;
    std::vector<int> a(n);
    double sum = 0;
    for (int i = 0; i < n; ++i) {
        std::cin >> a[i];
    }
    int l = 1, r = 1e8;
    while (l < r) {
        int m = l + (r - l) / 2;
        int sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += a[i] / m;
        }
        if (sum >= k) {
            l = m + 1;
        } else {
            r = m;
        }
    }
    std::cout << l - 1 << std::endl;
    return 0;
}