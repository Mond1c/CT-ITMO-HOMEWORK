//
// Created by pocht on 17.02.2023.
//
#include <iostream>
#include <utility>
#include <vector>
#include <functional>


int combine(int a, int b) {
    return a + b;
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
            data[v] = operation(data[v * 2], data[v * 2 + 1]);
        }
    }

    int findK(int v, int tl, int tr, int k) {
        if (k > data[v]) {
            return -1;
        }
        if (tl == tr) {
            return tl;
        }
        int tm = (tl + tr) / 2;
        if (data[v * 2] >= k) {
            return findK(v * 2, tl, tm, k);
        }
        return findK(v * 2 + 1, tm + 1, tr, k - data[v * 2]);
    }
};

int main() {
    int n;
    std::cin >> n;
    std::vector<int> a(n);
    for (int i = 0; i < n; ++i) {
        int b;
        std::cin >> b;
        a[i] = b == 0;
    }
    tree<int> t(n, combine);
    t.build(a, 1, 0, n - 1);
    int k;
    std::cin >> k;
    for (int i = 0; i < k; ++i) {
        std::string q;
        std::cin >> q;
        if (q == "u") {
            int pos, val;
            std::cin >> pos >> val;
            t.set(1, 0, n - 1, pos - 1, val == 0);
        } else {
            int l, r, m;
            std::cin >> l >> r >> k;
            std::cout << t.findK(1, l - 1, r - 1, m) << std::endl;
        }
    }
}