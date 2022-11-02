#include <iostream>
#include <vector>
#include <cmath>
using namespace std;
#define ull unsigned long long


size_t partition(vector<ull>& a, size_t l, size_t r) {
    ull x = a[r];
    size_t i = l - 1;
    for (size_t j = l; j <= r; ++j) {
        if (a[j] < x) {
            swap(a[++i], a[j]);
        }
    }
    swap(a[++i], a[r]);
    return i;
}

ull findK(vector<ull>& a, size_t k) {
    size_t l = 0, r = a.size() - 1;
    while (true) {
        size_t mid = partition(a, l, r);
        if (mid == k) {
            return a[mid];
        } else if (k < mid) {
            r = mid;
        } else {
            l = mid + 1;
        }  
    }
    return -1;
}

int main() {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    ull MOD = 1;
    for (int i = 0; i < 31; ++i) {
        MOD *= 2;
    }
    size_t n, k;
    cin >> n;
    vector<ull> a(n);
    cin >> a[0] >> k;
    for (size_t i = 1; i < n; ++i) {
        a[i] = (1103515245 * a[i - 1] + 12345) % MOD;
    }
    cout << findK(a, k) << endl;
}
