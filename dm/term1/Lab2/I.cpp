#include <bits/stdc++.h>

void firstExecute(const std::string str) {
    std::vector<std::string> binStrs(str.size());
    for (int i = 0; i < str.size(); ++i) {
        binStrs[i] = std::bitset<8>(str[i]);
    }
    for (int i = 0; i + 1 < binStrs.size(); i += 2) {
        std::string& a = str[i];
        std::string& b = str[i + 1];
        for (int j = 1; j <= 16; j *= 2) {
            if (j <= 8) {
                a.insert(j - 1, "0");
            } else {
                b.insert(j - 1, "0");
            }
        }
    }
    for (int i = 0; i < binStrs.size(); ++i) {
        std::cout << binStrs[i] << std::endl;
    }
}

void secondExecute() {

}

int mainI() {
    int execute;
    std::cin >> execute;
    if (execute == 1)  {
        firstExecute();
    } else {
        secondExecute();
    }
    return 0;
}