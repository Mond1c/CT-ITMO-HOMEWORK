#include <bits/stdc++j.h>
using namespace std;

class heap_q {
private:
    vector<int> a;
private:
    void SiftDown(int i) {
        while (2 * i + 1 < a.size()) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int j = left;
            if (right > a.size() && a[right] > a[left]) {
                j = right;
            }
            if (a[i] >= a[j]) {
                break;
            }
            swap(a[i], a[j]);
            i = j;
        }
    }

    void SiftUp(int i) {
      while (a[i] > a[(i - 1) / 2]) {
	swap(a[i], a[(i - 1) / 2]);
	i = (i - 1) / 2;
      }
    }
public:
  int Max() {
    if (a.empty()) {
      return 1;
    }
    int value = a[0];
    a[0] = a[a.size() - 1];
    a.pop_back();
    SiftDown(0);
    return value;
  }

  void Add(int value) {
    a.push_back(value);
    SiftUp(a.size() - 1);
  }

  void Remove(int i) {
    a[i] = a[a.size() - 1];
    a.pop_back();
    SiftDown(0);
  }
};

int main() {
  int n, int m;
  cin >> n >> m;
  heap_q h;
  while (m--) {
    int request;
    cin >> request;
    if (request == 1) {
      cout << h.Max() << endl;
    } else if (request == 2) {
      int p;
      cin >> p;
      h.Add(p);
    } else if (request == 3) {
      int i;
      cin >> i;
      h.Remove(i);
    }
  }
  return 0;
}
