#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
 
ull merge(vector<int>& a, vector<int>& b, int l, int h, int m) {
    int i = l, j = m + 1, k = l;;
    ull count = 0;
    while (i <= m && j <= h) {
        if (a[i] <= a[j]) {
            b[k++] = a[i++];
        } else {
            b[k++] = a[j++];
            count += (m - i + 1);
        }
    }
    while (i <= m) b[k++] = a[i++];
    for (i = l; i <= h; ++i) a[i] = b[i];
    return count;
}
 
ull sort(vector<int>& a, vector<int>& b, int l, int h) {
    if (h <= l) {
        return 0;
    }
    int m = (h - l) / 2 + l;
    ull count = 0;
    count += sort(a, b, l, m);
    count += sort(a, b, m + 1, h);
    count += merge(a, b, l, h, m);
    return count;
}
 
int main() {
    int n;
    cin >> n;
    vector<int> a(n), b(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
        b[i] = a[i];
    }
    cout << sort(a, b, 0, n - 1) << endl;
    return 0;
}