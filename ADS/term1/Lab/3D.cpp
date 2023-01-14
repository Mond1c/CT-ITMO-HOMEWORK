#include <bits/stdc++.h>

struct elem {
    int v, w, i;
    double f;
};


bool f(std::vector<elem> &elems, int n, int k, double m) {
    for (int i = 0; i < n; ++i) {
        elems[i].f = elems[i].w - elems[i].v / m;
    }
    std::sort(elems.begin(), elems.end(), [](const auto& lhs, const auto &rhs) {
        return lhs.f < rhs.f;
    });
    double sum = 0;
    for (int i = 0; i < k; ++i) {
        sum += elems[i].f;
    }
    return sum >= 0;
}

int main() {
    int n, k;
    std::cin >> n >> k;
    std::vector<elem> elems(n);
    for (int i = 0; i < n; ++i) {
        std::cin >> elems[i].v >> elems[i].w;
        elems[i].i = i + 1;
    }

    double l = 0, r = 10000001;
    while (r - l > 0.000000001) {
        double m = l + (r - l) / 2;
        if (f(elems, n, k, m)) {
            l = m;
        } else {
            r = m;
        }
    }

    for (int i = 0; i < k; ++i) {
        std::cout << elems[i].i << " ";
    }
    std::cout << std::endl;
}
