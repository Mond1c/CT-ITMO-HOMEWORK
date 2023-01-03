#include <iostream>
#include <vector>
using namespace std;

template<typename T>
ostream& operator<<(ostream& out, const vector<T>& a) {
	for (T value : a) out << value << " ";
	return out;
}

void anti_quick_sort(vector<int>& a) {
	for (size_t i = 2; i < a.size(); ++i) {
		    swap(a[i], a[i / 2]);
	}
}

int main() {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);
	int n;
	cin >> n;
	vector<int> a(n);
	for (int i = 0; i < n; ++i) a[i] = i + 1;
	anti_quick_sort(a);
	cout << a << endl;
}
