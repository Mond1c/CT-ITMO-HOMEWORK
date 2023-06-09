#include <bits/stdc++.h>
using namespace std;

class SegmentTree {
    vector<int> a;
    vector<int> t;
    int n;
public:
    SegmentTree(vector<int>& a) {
        this->n = a.size();
        this->a = a;
        this->t = vector<int>(4 * n);
        for (int i = 0; i < n; i++) {
            t[i + n] = a[i];
        }
        for (int i = n - 1; i > 0; i--) {
            t[i] = t[i << 1] + t[i << 1 | 1];
        }
    }

    void update(int pos, int value) {
        a[pos] = value;
        pos += n;
        t[pos] = value;
        while (pos > 1) {
            pos >>= 1;
            t[pos] = t[pos << 1] + t[pos << 1 | 1];
        }
    }


    int query(int l, int r, int k) {
        int res = 0;
        l += n; r += n;
        while (l <= r) {
            if (l & 1) {
                res += t[l];
                l++;
            }
            if (r & 1) {
                r--;
                res += t[r];
            }
            l >>= 1;
            r >>= 1;
        }
        if (k <= res) {
            return l - n;
        }
        return -1;
    }
};

int main() {
    int n;
    cin >> n;
    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    int m;
    cin >> m;
    SegmentTree t(a);
    for (int i = 0; i < m; i++) {
        char u;
        int l, r;
        cin >> u >> l >> r;
        if (u == 's') {
            int k;
            cin >> k;
            cout << abs(t.query(l - 1, r - 1, k)) << endl;
        } else {
            t.update(l - 1, r);
        }
    }
    return 0;
}

