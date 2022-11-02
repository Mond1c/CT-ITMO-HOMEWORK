#include <bits/stdc++.h>
using namespace std;

int binary_search(const vector<int>& a, int target) {
    int l = 0, r = a.size() - 1;
    while (l <= r) {
        int mid = l + (r - l) / 2;
        if (a[mid] == target) {
            return mid;
        } else if (a[mid] > target) {
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return -1;
}

int main() {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    int n, m;
    cin >> n >> m;
    vector<int> a(n);
    for (int i = 0; i < n; ++i) {
        cin >> a[i];
    }
    for (int i = 0; i < m; ++i) {
        int b;
        cin >> b;
        int index = binary_search(a, b);
        if (index == -1) {
            cout << 0 << endl;
            continue;
        }
        int s = index, e = index;
        while (s - 1 >= 0 && a[s - 1] == b) --s;
        while (e + 1 < n && a[e + 1] == b) ++e;
        cout << s + 1 << " " << e + 1 << endl;
    }
    return 0;
}
