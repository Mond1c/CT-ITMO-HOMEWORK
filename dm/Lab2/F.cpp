#include <bits/stdc++.h>

int mainF() {
    std::unordered_map<int, std::string> dict;
    for (int i = 0; i < 26; ++i) {
        dict[i] = std::string(1, (char) ('a' + i));
    }

    int n;
    std::cin >> n;
    std::string prev, next;
    int code = 26;
    int a;
    std::cin >> a;
    prev = dict[a];
    std::cout << dict[a];
    for (int i = 1; i < n; ++i) {
        std::cin >> a;
        std::cout << dict[a];
        next = dict[a];
        std::string t = prev + next.substr(0, prev.size());
        if (std::find_if(dict.begin(),
                         dict.end(), [&t](const auto& lhs) {
            return lhs.second == t;
        }) == dict.end()) dict[code++] = t;
        prev = next.substr(prev.size(), next.size() - prev.size());
    }
    std::cout << std::endl;
    return 0;
}
