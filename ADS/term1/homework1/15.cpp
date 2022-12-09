#include <iostream>
#include <vector>
#include <cmath>
#include <unordered_map>
using namespace std;

int get_count(vector<int>& a, vector<int>& b) {
    int n = a.size(), m = b.size();
    int count = 0;
    int i = 0, j = 0;
    while (i < n && j < m) {
        if (a[i] < b[j]) {
            ++i;
        } else if (a[i] > b[j]) {
            ++j;
        } else {
            if (i + 1 < n && a[i + 1] == b[j]) {
                ++i;
            } else if (j + 1 < m && b[j + 1] == a[i]) {
                ++j;
            } else {
                ++i;
                ++j;
            }
            ++count;
        }
    }
    return count;
}

int main() {
    vector<int> a = {1, 2, 2, 6};
    vector<int> b = {0, 2, 5};
    cout << get_count(a, b) << endl;
}
