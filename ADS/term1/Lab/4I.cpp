#include <bits/stdc++.h>

int main() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    long long m = 0;
    int n;
    std::cin >> n;
    std::deque<long long> q;
    for (int i = 0; i < n; i++) {
        int t;
        std::cin >> t;
        if (t == 1) {
            long long a;
            std::cin >> a;
            q.push_back(a - m);
        } else if (t == 2) {
            long long x, y;
            std::cin >> x >> y;
            m += y;
            q[0] += x - y;
        } else {
            std::cout << q.front() + m << std::endl;
            q.pop_front();
        }
    }
    return 0;
}