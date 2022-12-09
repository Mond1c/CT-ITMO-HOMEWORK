#include <bits/stdc++.h>
int mainC() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    std::string str;
    std::cin >> str;
    std::vector<std::string> strs(str.size());
    for (int i = 0; i < str.size(); ++i) {
        for (int j = 0; j < str.size(); ++j) {
            strs[j] = str[j] + strs[j];
        }
        std::sort(strs.begin(), strs.end());
    }
    std::cout << strs[0] << std::endl;
}