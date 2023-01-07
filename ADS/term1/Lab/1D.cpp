#include <bits/stdc++.h>
using namespace std;

int count = 0;

void merge(vector<int>& a, int l, int m, int r) {
    int n1 = m - l + 1;
    int n2 = r - m;

    vector<int> left(n1), right(n2);
    for (int i = 0; i < n1; i++) {
        left[i] = a[l + i];
    }
    for (int i = 0; i < n2; i++) {
        right[i] = a[m + 1 + i];
    }
    int i = 0, j = 0;
    int k = l;
    while (i < n1 && j < n2) {
        if (left[i] <= right[j]) {
            arr[k] = left[i];
            i++;
            count += ();
        } else {
            arr[k] = right[j];
            j++;
        }
        k++;
    }

    while (i < n1) {
        arr[k++] = left[i++];
    }

    while (j < n2) {
        arr[k++] = right[j++];
    }
}


void sort(vector<int>& a, int l, int r) {
    if (l >= r) return;
    int m = l + (r - l) / 2;
    sort(a, l, m);
    sort(a, m + 1, r);

    merge(a, l, m, r);
}
