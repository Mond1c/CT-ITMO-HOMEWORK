#include <bits/stdc++.h>
using namespace std;

void sortOnePart(vector<string>& strings, int n, int m, int k) {
    vector<vector<int>> counter(26);
    for (int i = 0; i < n; ++i) {
        counter[strings[i][m - k - 1] - 'a'].push_back(i);
    }
    int p = 0;
    vector<string> s(n);
    for (int i = 0; i < 26; ++i) {
        for (int j = 0; j < counter[i].size(); ++j) {
            s[p++] = strings[counter[i][j]];
        }
    }
    for (int i = 0; i < n; ++i) {
        strings[i] = s[i];
    }
}

int main() {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    int n, m, k;
    cin >> n >> m >> k;
    vector<string> strings(n);
    for (int i = 0; i < n; ++i) {
        cin >> strings[i];
    }
    for (int i = 0; i < k; ++i) {
        sortOnePart(strings, n, m, i);
    }
    for (int i = 0; i < n; ++i) {
        cout << strings[i] << endl;
    }
    return 0;
}
