#include <bits/stdc++.h>

int fl(int l) {
    if (l == 1) {
        return 0;
    }
    return fl(l / 2) + 1;
}

long long fp(long long a, long long b) {
    long long res = 1;
    while (b) {
        if (b % 2 == 0) {
            b /= 2;
            a *= a;
        } else {
            b -= 1;
            res *= a;
            b /= 2;
            a *= a;
        }
    }
    return res;
}

int main() {
    long long n, m, a1;
    std::cin >> n >> m >> a1;
    int k = fl(n);
    std::vector<std::vector<int>> st(n, std::vector<int>(k));
    for (int i = 0; i < n - k; i++) {
        for (int j = 0; j < k; j++) {
            if (j == 0) {
                st[i][j] = a1;
                a1 = (23 * a1 + 21536) % 16714589;
            } else {
                st[i][j] = std::min(st[i][j - 1], st[i + fp(2, j - 1)][j - 1]);
            }
        }
    }
    int u, v;
    std::cin >> u >> v;
    int ans = 0;
    for (int i = 1; i <= m; i++) {
        int j = fl(v - u);
        ans = std::min(st[u - 1][j], st[v - fp(2, j)][j]);
        u = ((17 * u + 751 + ans + 2 * i) % n) + 1;
        v = ((13 * v + 593 + ans + 5 * i) % n) + 1;
    }
    return 0;
}
