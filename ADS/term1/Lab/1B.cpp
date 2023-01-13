#include <iostream>
#include <vector>
#include <cmath>
using namespace std;
#define ull unsigned long long
 
 
 
ull partition(vector<ull>& a, size_t l, size_t r) {
	if (l == r) {
		return l;
	}
	size_t i = l, j = r;
	while (true) {
		while (a[i] < a[r]) ++i;
		while (a[j] > a[r]) --j;
		if (i >= j) break;
		swap(a[i++], a[j--]);
	}
	std::swap(a[i], a[r]);
	return i;
}
 
ull find_k(vector<ull>& a, size_t k) {
	size_t l = 0, r = a.size() - 1;
	while (true) {
		size_t m = partition(a, l, r);
		if (m == k) return a[k];
		if (k < m) r = m - 1;
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