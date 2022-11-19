#include <bits/stdc++.h>

int mainE() {
    std::string str;
    std::cin >> str;
    std::unordered_map<std::string, int> mp;
    for (int i = 0; i < 26; ++i) {
        mp[std::string(1, ((char)('a' + i)))] = i;
    }
    std::string t;
    int code = 26;
    for (int i = 0; i < str.size(); ++i) {
        std::string c = t + std::string(1, str[i]);
        if (mp.count(t + std::string(1, str[i]))) {
            t = t + str[i];
        } else {
            std::cout << mp[t] << " ";
            mp[t + str[i]] = code++;
            t = str[i];
        }
    }
    std::cout << mp[t] << " ";
    std::cout << std::endl;
    return 0;
}