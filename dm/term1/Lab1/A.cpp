#include <iostream>
#include <vector>

void get_properties(std::vector<std::vector<int>> relationship, int n) {
	int i, j, k;
	{
		bool reflexivity = true;
		for (i = 0; i < n; ++i) {
			if (!relationship[i][i]) {
				reflexivity = false;
				break;
			}
		}
		std::cout << reflexivity << " ";
	}
	{
		bool anti_reflexivity = true;
		for (i = 0; i < n; ++i) {
			if (relationship[i][i]) {
				anti_reflexivity = false;
				break;
			}
		}
		std::cout << anti_reflexivity << " ";
	}
	{
		bool symmetry = true;
		for (i = 0; i < n; ++i) {
			for (j = 0; j < n; ++j) {
				if (relationship[i][j] && !relationship[j][i]) {
					symmetry = false;
					break;
				}
			}
		}
		std::cout << symmetry << " ";
	}
	{
		bool anti_symmetry = true;
		for (i = 0; i < n; ++i) {
			for (j = 0; j < n; ++j) {
				if (relationship[i][j] && relationship[j][i] && i != j) {
					anti_symmetry = false;
					break;
				}
			}
		}
		std::cout << anti_symmetry << " ";
	}
	{
		bool transitivity = true;
		for (i = 0; i < n; ++i) {
			for (j = 0; j < n; ++j) {
				for (k = 0; k < n; ++k) {
					if (relationship[i][j] && relationship[j][k] &&
						!relationship[i][k]) {
						transitivity = false;
						break;
					}
				}
			}
		}
		std::cout << transitivity << " ";
	}
}

int mainA() {
	std::cin.tie(nullptr);
	std::ios::sync_with_stdio(false);
	int n;
	std::cin >> n;
	std::vector<std::vector<int>> relationship1(n, std::vector<int>(n)),
		relationship2(n, std::vector<int>(n));
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < n; ++j) {
			std::cin >> relationship1[i][j];
		}
	}
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < n; ++j) {
			std::cin >> relationship2[i][j];
		}
	}
	get_properties(relationship1, n);
	std::cout << std::endl;
	get_properties(relationship2, n);

	std::vector<std::vector<int>> composition(n, std::vector<int>(n));
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < n; ++j) {
			bool value = false;
			for (int k = 0; k < n; ++k) {
				if (relationship1[i][k] && relationship2[k][j]) {
					value = true;
					break;
				}
			}
			composition[i][j] = value;
		}
	}
	std::cout << std::endl;
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < n; ++j) {
			std::cout << composition[i][j] << " ";
		}
		std::cout << std::endl;
	}
	return 0;
}