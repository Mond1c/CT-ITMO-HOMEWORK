//
// Created by mond1c on 11/18/22.
//
#include <bits/stdc++.h>

int mainB() {
    std::string str;
    std::cin >> str;
    //std::cout << 123 << std::endl;
    std::vector<std::string> strs;
    strs.push_back(str);
    for (int i = 1; i < str.size(); ++i) {
        str = str.back() + str.substr(0, str.size() - 1);
        //std::cerr << str << std::endl;
        strs.push_back(str);
    }
    std::sort(strs.begin(), strs.end());
    str = "";
    for (auto & i : strs) {
        str += i.back();
    }
    std::cout << str << std::endl;
    return 0;
}