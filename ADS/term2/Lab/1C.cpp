#include <iostream>
#include <utility>
#include <vector>
#include <functional>

std::vector<int> t;
int n;
void build (std::vector<int> a, int v, int tl, int tr) {
	if (tl == tr)
		t[v] = a[tl];
	else {
		int tm = (tl + tr) / 2;
		build (a, v*2, tl, tm);
		build (a, v*2+1, tm+1, tr);
		t[v] = t[v*2] + t[v*2+1];
	}
}

int sum (int v, int tl, int tr, int l, int r) {
	if (l > r)
		return 0;
	if (l == tl && r == tr)
		return t[v];
	int tm = (tl + tr) / 2;
	return sum (v*2, tl, tm, l, std::min(r,tm))
		+ sum (v*2+1, tm+1, tr, std::max(l,tm+1), r);
}

void update (int v, int tl, int tr, int pos, int new_val) {
	if (tl == tr)
		t[v] = new_val;
	else {
		int tm = (tl + tr) / 2;
		if (pos <= tm)
			update (v*2, tl, tm, pos, new_val);
		else
			update (v*2+1, tm+1, tr, pos, new_val);
		t[v] = t[v*2] + t[v*2+1];
	}
}

int main() {
    std::cin >> n;
    std::vector<int> a(n);
    for (long long i = 0; i < n; ++i) {
        long long value;
        std::cin >> value;
        a[i] = (value == 0);
    }
    t.resize(n * 4);
    build(a, 1, 0, n - 1);
    long long k;

    std::cin >> k;
    for (long long i = 0; i < k; ++i) {
        std::string query;
        std::cin >> query;
        if (query == "QUERY") {
            long long l, r;
            std::cin >> l >> r;
            std::cout << sum(1, 0, n - 1, l - 1, r - 1) << std::endl;
        } else {
            long long i, x;
            std::cin >> i >> x;
            x = (x == 0);
            update(1, 0, n - 1, i - 1, x);
        }
    }
}