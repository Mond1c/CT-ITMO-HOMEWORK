#include <bits/stdc++.h>

int main4I() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    int n;
    std::cin >> n;
    std::deque<int> q;
    for (int i = 0; i < n; i++) {
        int t;
        std::cin >> t;
        if (t == 1) {
            int a;
            std::cin >> a;
            q.push_back(a);
        } else if (t == 2) {
            int x, y;
            std::cin >> x >> y;
            q[0] += x;
            for (int j = 1; j < q.size(); j++) q[j] += y;
        } else {
            std::cout << q.front() << std::endl;
            q.pop_front();
        }
    }
    return 0;
}