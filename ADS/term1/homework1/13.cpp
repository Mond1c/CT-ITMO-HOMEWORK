#include <iostream>
#include <vector>
#include <cmath>
#include <unordered_map>
using namespace std;

pair<int, int> find_sum(vector<int>& a, vector<int>& b, int s) {
    unordered_map<int, int> mp_a, mp_b;
    int i = 0, j = 0;
    int n = a.size(), m = b.size();
    while ( i + j < n + m + 1) {
        if (i < n) {
            int diff = s - a[i];
            if (mp_b.count(diff)) return {i, mp_b[diff]};
            else mp_a[a[i]] = i;
            ++i;
        }
        if (j < m) {
            int diff = s - b[j];
            if (mp_a.count(diff)) return {mp_a[diff], j};
            else mp_b[b[j]] = j;
            ++j;
        }
    }
    return {-1, -1};
}

int main() {
    vector<int> a = {1, 2, 3, 4};
    vector<int> b = {7, 8, 9, 12};
    auto ans = find_sum(a, b, 11);
    cout << ans.first << " " << ans.second << endl;
}
