#include <iostream>
#include <vector>
#include <string>

int mainH() {
	int n;
	std::cin >> n;
	std::string formula = "((A0|B0)|(A0|B0))";
	for (int i = 1; i < n; ++i) {
		std::string index = std::to_string(i);
		formula = "((" + formula + "|((A" + index + "|A" + index + ")|(B" + index +
			"|B" + index + ")))|(A" + index + "|B" + index + "))";
	}
	std::cout << formula << std::endl;
	return 0;
}