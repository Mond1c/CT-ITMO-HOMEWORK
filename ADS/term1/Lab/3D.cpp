#include <bits/stdc++.h>

int main() {
    int n, k;
    std::cin >> n >> k;
    double k_best;
    double v0, w0;
    std::cin >> v0 >> w0;
    std::vector<std::pair<int, double>> pairs;
    pairs.push_back({0, v0 / w0});
    double prev_v = w0;
    double prev_w = v0;
    for (int i = 1; i < n; ++i) {
        double best = prev_v / prev_w;
        double v, w;
        std::cin >> v >> w;
        double cur_v = prev_v + v;
        double cur_w = prev_w + w;
        if (cur_v / cur_w > best) {
            prev_v = cur_v;
            prev_w = cur_w;
            pairs.push_back({i, v / w});
        }
    }
    std::sort(pairs.begin(), pairs.end(), [](const auto& lhs, const auto& rhs) {
        return lhs.second > rhs.second;
    });
    std::cout << pairs.size() << std::endl;
    for (int i = 0; i < k; ++i) {
        std::cout << pairs[i].first << " ";
    }
    std::cout << std::endl;
}