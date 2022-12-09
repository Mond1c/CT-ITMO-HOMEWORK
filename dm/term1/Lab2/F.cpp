#include <bits/stdc++.h>

int mainF() {
    std::unordered_map<int, std::string> dict;
    std::unordered_set<std::string> st;
    for (int i = 0; i < 26; ++i) {
        dict[i] = std::string(1, (char) ('a' + i));
        st.insert(std::string(1, (char) ('a' + i)));
    }
    int code = 26;
    int n;
    std::cin >> n;
    std::string s, v;
    int a;
    std::cin >> a;
    s = dict[a];
    std::cout << dict[a];
    for (int i = 1; i < n; ++i) {
        std::cin >> a;
        if (dict.count(a)) {
            v = dict[a];
        } else {
            v = s + s[0];
        }
        std::cout << v;
        dict[code++] = s + v[0];
        s = v;
    }
    return 0;
}
