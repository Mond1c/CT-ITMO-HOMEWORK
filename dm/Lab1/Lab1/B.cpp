#include <iostream>
#include <string>
#include <vector>

std::string get_polynom1(const std::string& function) {
	std::vector<std::string> triangle(function.size());
	triangle[0] = function;
	for (int i = 1; i < function.size(); ++i) {
		for (int j = 0; j < function.size() - i; ++j) {
			triangle[i] += '0' + ((triangle[i - 1][j] - '0' + triangle[i - 1][j + 1] - '0') % 2);
		}
	}
	std::string polynom;
	for (int i = 0; i < function.size(); ++i) {
		polynom += triangle[i][0];
	}
	return polynom;
}

bool check_mon(const std::string& function, int l, int r) {
	if (l >= r - 1) return true;
	int mid = l + (r - l) / 2;
	for (int i = l, j = mid; i < mid; ++i, ++j) {
		if (function[i] > function[j]) {
			return false;
		}
	}
	return check_mon(function, l, mid) && check_mon(function, mid, r);
}

int mainB() {
	int n;
	std::cin >> n;
	std::vector<std::pair<int, std::string>> functions(n);
	int i;
	for (i = 0; i < n; ++i) {
		std::cin >> functions[i].first >> functions[i].second;
	}
	bool zero_save = false;
	bool one_save = false;
	for (i = 0; i < n; ++i) {
		if (functions[i].second.front() == '1') {
			zero_save = true;
		} 
		if (functions[i].second.back() == '0') {
			one_save = true;
		}
		if (zero_save && one_save) {
			break;
		}
	}
	if (!zero_save || !one_save) {
		std::cout << "NO" << std::endl;
		return 0;
	}

	bool self = false;
	for (i = 0; i < n; ++i) {
		bool fun_self = check_mon(functions[i].second, 0, functions[i].second.size());
		if (!fun_self) {
			self = true;
			break;
		}
	}
	if (!self) {
		std::cout << "NO" << std::endl;
		return 0;
	}
	bool mon = false;
	for (i = 0; i < n; ++i) {
		bool fun_mon = true;
		for (int j = 1; j < functions[i].second.size(); ++j) {
			if (functions[i].second[j - 1] > functions[i].second[j]) {
				fun_mon = false;
			}
		}
		if (!fun_mon || n == 1) {
			mon = true;
			break;
		}
	}
 	if (!mon) {
		std::cout << "NO" << std::endl;
		return 0;
	}
	bool linear = false;
	for (int i = 0; i < functions.size(); ++i) {
		std::string polynom = get_polynom1(functions[i].second);
		bool fun_linear = true;
		int t = 1;
		for (int j = 1; j < functions[i].second.size(); ++j) {
			if (polynom[j] == '1') {
				if (j != t) {
					fun_linear = false;
					break;
				}
				t *= 2;
			}
			else if (polynom[j] == '0' && j == t) {
				t *= 2;
			}
		}
		if (!fun_linear) {
			linear = true;
			break;
		}
	}
	if (!linear) {
		std::cout << "NO" << std::endl;
		return 0;
	}
	std::cout << "YES" << std::endl;
}