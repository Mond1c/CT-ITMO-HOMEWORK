#include <iostream>
#include <vector>
#include <cmath>
#include <string>
#include <algorithm>
#include <bitset>

int findDepth(std::vector<std::vector<int>>& ins, int variables, int cur) {
	if (cur + variables < variables) return 0;
	int d = 0;
	for (int i = 0; i < ins[cur].size(); ++i) {
		d = std::max(d, findDepth(ins, variables, ins[cur][i] - 1 - variables));
	}
	return d + 1;
}

int getValue(std::vector<std::vector<int>>& ins, std::vector<std::vector<int>> outs,
	int variables, int cur, const std::string& binStr) {
	if (cur <= variables) {
		return binStr[binStr.size() - variables + cur - 1] - '0';
	}
	std::vector<int> values(ins[cur - variables - 1].size());
	std::string res;
	for (int i = 0; i < ins[cur - variables - 1].size(); ++i) {
		res += std::to_string(getValue(ins, outs, variables, ins[cur - variables - 1][i], binStr));
	}
	return outs[cur - variables - 1][std::stoi(res, nullptr, 2)];
}

int main() {
	int n;
	std::cin >> n;
	if (n == 1) {
		std::cout << 1 << std::endl;
		std::cout << "01" << std::endl;
		return 0;
	}
	std::vector<std::vector<int>> ins;
	std::vector<std::vector<int>> outs;
	int variables = 0;
	for (int i = 0; i < n; ++i) {
		int m;
		std::cin >> m;
		if (m == 0) {
			++variables;
		}
		else {
			std::vector<int> in(m);
			std::vector<int> out(std::pow(2, m));
			for (int j = 0; j < m; ++j) {
				std::cin >> in[j];
			}
			for (int j = 0; j < out.size(); ++j) {
				std::cin >> out[j];
			}
			ins.push_back(in);
			outs.push_back(out);
		}
	}
	std::cout << findDepth(ins, variables, ins.size() - 1) << std::endl;

	int start = 0;
	int max_in = 0;
	for (int i = 0; i < ins.size(); ++i) {
		for (int j = 0; j < ins[i].size(); ++j) {
			if (ins[i][j] > max_in) {
				max_in = ins[i][j];
				start = i;
			}
		}
	}

	for (int i = 0; i < std::pow(2, variables); ++i) {
		std::string binStr = std::bitset<32>(i).to_string();
		std::cout << getValue(ins, outs, variables, start + 1 + variables, binStr);
	}
	std::cout << std::endl;

	return 0;
}