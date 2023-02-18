#include <iostream>
#include <utility>
#include <vector>
#include <functional>

struct Node {
	long long val=0;
	long long pos=0;
};

Node combine(Node a, Node b) {
    if (a.val > b.val) {
		return Node{a.val, a.pos};
	}
	return Node{b.val, b.pos};
}

template<typename T>
struct tree {
	std::vector<T> data;
	std::function<T(T a, T b)> operation;

	tree(int n, std::function<T(T a, T b)> func) : operation(std::move(func)) {
        data.resize(n * 4);
    }

	void build(std::vector<T>& a, int v, int l, int r) {
        if (l == r) {
			data[v] = a[l];
		} else {
			int m = (l + r) / 2;
			build(a, v * 2, l, m);
			build(a, v * 2 + 1, m + 1, r);
			data[v] = operation(data[v * 2], data[v * 2 + 1]);
		}
	}

	T get(int v, int tl, int tr, int l, int r) {
        if (l <= tl && tr <= r) {
            return data[v];
        }
        if (tr < l || r < tl) {
            return T{};
        }
        int tm = (tl + tr) / 2;
        T left = get(v * 2, tl, tm, l, r);
        T right = get(v * 2 + 1, tm + 1, tr, l, r);
        return operation(left, right);
	}

	void set(int v, int tl, int tr, int pos, T val) {
		if (tl == tr) {
			data[v] = val;
		} else {
			int tm = (tl + tr) / 2;
			if (pos <= tm) {
				set(v * 2, tl, tm, pos, val);
			} else {
				set(v * 2 + 1, tm + 1, tr, pos, val);
			}
			data[v] = operation(data[v * 2], data[v * 2] + 1);
		}
	}
};

int main() {
	int n;
	std::cin >> n;
	std::vector<Node> a(n);
	for (int i = 0; i < n; ++i) {
		std::cin >> a[i].val;
		a[i].pos = i;
	}
	tree<Node> t(n, combine);
	t.build(a, 1, 0, n - 1);
	int k;
	std::cin >> k;
	for (int i = 0; i < k; ++i) {
		int l, r;
		std::cin >> l >> r;
		Node ans = t.get(1, 0, n - 1, l - 1, r - 1);
		std::cout << ans.val << " " << ans.pos + 1 << std::endl;
	}
}