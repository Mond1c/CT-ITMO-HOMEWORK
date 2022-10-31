#include <iostream>
#include <vector>
#include <cmath>
using namespace std;
#define ull unsigned long long



ull partition(vector<ull>& a, size_t l, size_t r) {
  ull x = a[l + (r - l) / 2];
  while (l <= r) {
    while (a[l] < x) ++l;
    while (a[r] > x) --r;
    if (l >= r) break;
    swap(a[l++], a[r--]);
  }
  return r;
}

ull find_k(vector<ull>& a, size_t k) {
  size_t l = 0, r = a.size() - 1;
  while (true) {
    size_t m = partition(a, l, r);
    if (m == k) return a[k];
    if (k < m) r = m;
    else l = m + 1;
  }
}


int main() {
    ull MOD = 1;
    for (int i = 0; i < 31; ++i) MOD *= 2;

    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    size_t n;
    cin >> n;
    vector<ull> a(n);
    size_t k;
    cin >> a[0] >> k;
    for (size_t i = 1; i < n; ++i) a[i] = (1103515245 * a[i - 1] + 12345) % MOD;
    cout << find_k(a, k) << endl;
    return 0;
}