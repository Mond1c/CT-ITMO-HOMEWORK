#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

void comp(int& a, int& b) {
  if (b < a) {
    swap(a, b);
  }
}

bool is_sorted(std::vector<int>& a) {
  for (int i = 1; i < a.size(); i++) {
    if (a[i] < a[i - 1]) return false;
  }
  return true;
}

void generate(vector<int>& a, int n) {
  vector<int> b;
  while (n) {
    b.push_back(n % 2);
    n /= 2;
  }
  for (int i = b.size() - 1, j = a.size() - 1; i >= 0; j--, i--) {
    a[j] = b[i];
  }
}

int main() {
    int n, m, k;
    cin >> n >> m >> k;
    std::vector<std::pair<int, int>> network;
    for (int i = 0; i < k; i++) {
      int l;
      std::cin >> l;
      while (l--) {
	int a, b;
	std::cin >> a >> b;
	network.push_back({a, b});
      }
    }
    for (int i = 0; i < std::pow(2, n); i++) {
      vector<int> a(n);
      generate(a, i);
      for (const auto& p : network) {
	comp(a[p.first - 1], a[p.second - 1]);
      }
      if (!is_sorted(a)) {
	cout << "No" << endl;
	return 0;
      }
    }
    cout << "Yes" << endl;
    return 0;
}
