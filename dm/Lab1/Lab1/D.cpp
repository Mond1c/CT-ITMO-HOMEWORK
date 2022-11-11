#include <iostream>
#include <vector>
#include <unordered_map>
int mainD() {
	int n;
	std::cin >> n;
	const int m = std::pow(2, n);
	std::vector<std::pair<int, std::vector<int>>> elements;
	std::vector<int> indexes;
	std::unordered_map<int, std::pair<int, std::vector<int>>> negative;
	for (int i = 0; i < m; ++i) {
		std::string ins;
		int out;
		std::cin >> ins >> out;
		if (out == 0) continue;
		int start = elements.size() + 1;
		for (int j = 0; j < ins.size(); ++j) {
			if (ins[j] == '0') {
				if (!negative.contains(j)) {
					negative[j] = { (int)elements.size() + 1, {j + 1}};
					elements.push_back({ 1, {j + 1} });
				}
			}
		}
		for (int j = 1; j < ins.size(); ++j) {
			int k = j;
			if (j != 1) {
				k = elements.size() + n;
			}
			int l = j + 1;
			if (k == j && ins[j - 1] == '0') {
				k = negative[j - 1].first + n;
			}
			if (ins[j] == '0') {
				l = negative[j].first + n;
			}
			elements.push_back({ 2, {std::min(k, l), std::max(k, l)}});
		}
		indexes.push_back(elements.size() - 1);
	}
	for (int i = 1; i < indexes.size(); ++i) {
		if (i == 1) {
			elements.push_back({ 3, {indexes[i - 1] + 1 + n, indexes[i] + 1 + n} });
		}
		else {
			elements.push_back({ 3, {std::min((int)elements.size() + n, indexes[i] + 1 + n),
				std::max((int)elements.size() + n, indexes[i] + 1 + n)}});
		}
	}
	std::cout << elements.size() + n << std::endl;
	for (int i = 0; i < elements.size(); ++i) {
		std::cout << elements[i].first << " ";
		for (int j : elements[i].second) std::cout << j << " ";
		std::cout << std::endl;
	}
	return 0;
}