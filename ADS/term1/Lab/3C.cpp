#include <bits/stdc++.h>

double EPS = 0.0000000001;

double f(double h1, double h2) {
    return (h1 + h2) / 2 - 1;
}

double binary_search(std::vector<double> &h, int n) {
    double l = 0;
    double r = h[0];
    while (r - l > EPS) {
        h[1] = l + (r - l) / 2;
        bool is_l = false;
        for (int i = 2; i < n; ++i) {
            h[i] = 2 * h[i - 1] + 2 - h[i - 2];
            if (h[i] <= 0) {
                is_l = true;
                l = h[1];
                break;
            }
        }
        if (!is_l) {
            r = h[1];
        }
    }
    return h[n - 1];
}

int main() {
    int n;
    std::cin >> n;
    std::vector<double> h(n);
    std::cin >> h[0];
    printf("%.2f\n", binary_search(h, n));
}