#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

constexpr unsigned long long MOD = pow(2, 31);

unsigned long long partition(vector<unsigned long long>& a, int l, int r) {
	unsigned long long x = a[l + (r - l) / 2];
	while (l <= r) {
		while (a[l] < x) l++;
		while (a[r] > x) r--;
		if (l >= r) break;
		swap(a[l++], a[r--]);
	}
	return r;
}

unsigned long long solve(vector<unsigned long long>& a, int k) {
	int l = 0, r = a.size() - 1;
	while (true) {
		int mid = partition(a, l, r);
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
	int n, k;
	cin >> n;
	vector<unsigned long long> a(n);
	cin >> a[0] >> k;
	for (int i = 1; i < n; i++) {
		a[i] = (1103515245 * a[i - 1] + 12345) % MOD;
	}
	cout << solve(a, k) << endl;
	return 0;
}
