#include <iostream>
#include <vector>

int mainF() {
	int n, k;
	std::cin >> n >> k;
	std::vector<std::vector<int>> x(k, std::vector<int>(n));
	for (int i = 0; i < k; ++i) {
		for (int j = 0; j < n; ++j) {
			std::cin >> x[i][j];
		}
	}
	std::vector<bool> visisted(k);
	bool c = true;
	while (c) {
		c = false;
		for (int i = 0; i < k; ++i) {
			if (visisted[i]) continue;
			int unique_index = 0;
			int count = 0;
			for (int j = 0; j < n; ++j) {
				if (x[i][j] != -1) {
					++count;
					unique_index = j;
				}
				if (count > 1) break;
			}
			if (count != 1) continue;
			c = true;
			visisted[i] = true;
			for (int j = 0; j < k; ++j) {
				if (i == j || visisted[j] || x[j][unique_index] == -1) continue;
				int does_not_exist = 0;
				int negative = 0;
				for (int m = 0; m < n; ++m) {
					if (m == unique_index) {
						if (x[j][m] == x[i][m]) {
							visisted[j] = true;
							break;
						}
						negative++;
					}
					else if (x[j][m] == -1) {
						does_not_exist++;
					}
				}
				x[j][unique_index] = -1;
				if (negative + does_not_exist == n) {
					std::cout << "YES" << std::endl;
					return 0;
				}
			}
		}
	}

	std::cout << "NO" << std::endl;
	return 0;
}