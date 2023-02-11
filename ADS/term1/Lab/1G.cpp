#include <bits/stdc++.h>

int main() {
	int n, m, p;
	std::cin >> n >> m >> p;
	std::priority_queue<int> q;
	for (int i = 0; i < n; ++i) {
		int x;
		std::cin >> x;
		q.push(x);
	}
	long long count = 0;
	int vedro = 0;
	while (vedro < m) {
		if (q.empty()) break;
		int x = q.top(); q.pop();
		count += std::min(p, x);
		x -= p;
		if (x > 0) q.push(x);
		vedro++;
	}

	std::cout << count << std::endl;

	return 0;
}