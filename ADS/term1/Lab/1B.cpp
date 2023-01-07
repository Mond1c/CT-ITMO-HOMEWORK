#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

constexpr unsigned long long pow2(int i) {
    unsigned long long ans = 1;
    while (i--) ans *= 2;
    return ans;
}

constexpr unsigned long long MOD = pow2(31);

unsigned long long solve(vector<unsigned long long>& a, unsigned long long k) {
	unsigned long long l = 0, r = a.size() - 1;
	while (true) {
		auto pivot = *next(a.begin() + l, distance(a.begin() + l, a.begin() + r + 1) / 2);
		unsigned long long mid = partition(a.begin() + l, a.begin() + r + 1, [&pivot](const auto& x) { return x < pivot; }) - a.begin();
		if (mid == k) {
			return a[k];
		}
		if (k < mid) {
			r = mid;
		} else {
			l = mid + 1;
		}
	}
}

int main() {
	cin.tie(nullptr);
	ios::sync_with_stdio(false);
	unsigned long long n, k;
	cin >> n;
	vector<unsigned long long> a(n);
	cin >> a[0] >> k;
	for (unsigned long long i = 1; i < n; i++) {
		a[i] = (1103515245 * (unsigned long long)a[i - 1] + 12345) % MOD;
	}
	cout << solve(a, k) << endl;
	return 0;
}
