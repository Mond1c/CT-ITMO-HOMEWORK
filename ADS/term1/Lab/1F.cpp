#include <bits/stdc++j.h>
using namespace std;

class heap_q {
private:
    vector<int> a;
private:
    void siftDown(int i) {
        while (2 * i + 1 < a.size()) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int j = left;
            if (right < a.size() && a[right] < a[left]) {
                j = right;
            }
            if (a[i] <= a[j]) {
                break;
            }
            swap(a[i], a[j]);
            i = j;
        }
    }

    void siftUp(int i) {

    }
};
