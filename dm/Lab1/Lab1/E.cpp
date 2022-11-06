#include <iostream>
#include <string>
#include <vector>

std::string get_polynom(const std::string& function) {
	std::vector<std::string> triangle(function.size());
	triangle[0] = function;
	for (int i = 1; i < function.size(); ++i) {
		for (int j = 0; j < function.size() - i; ++j) {
			triangle[i] += '0' + ((triangle[i - 1][j] - '0' + triangle[i - 1][j + 1] - '0')) % 2;
		}
	}
	std::string polynom;
	for (int i = 0; i < function.size(); ++i) {
		polynom += triangle[i][0];
	}
	return polynom;
}

int main() {
	int n;
	std::cin >> n;
	n = pow(2, n);
	std::string function(n, ' ');
	std::vector<std::string> arguments(n);
	for (int i = 0; i < n; ++i) {
		std::cin >> arguments[i] >> function[i];
	}
	std::string polynom = get_polynom(function);
	for (int i = 0; i < n; ++i) {
		std::cout << arguments[i] << " " << polynom[i] << std::endl;
	}
}