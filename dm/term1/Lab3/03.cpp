#include <vector>

std::vector<std::vector<bool>> generateGrayCode(int n) {
	std::vector<std::vector<bool>> codes(n, std::vector<bool>(n));
	codes[0][n - 1] = 0;
	codes[1][n - 1] = 1;
	int k = 2;
	for (int i = 1; i < n; i++) {
		int t = k;
		for (int j = k + 1; j < k * 2; j++) {
			codes[j] = codes[t];
			codes[t][n - i] = 0;
			codes[j][n - i] = 1;
			t--;
		}
		k *= 2;
	}
}