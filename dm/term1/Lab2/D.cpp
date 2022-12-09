#include <bits/stdc++.h>

template<typename T>
std::ostream& operator<<(std::ostream& out, const std::vector<T>& a) {
    for (int i = 0; i < a.size(); ++i)
        out << a[i] << " ";
    return out;
}

int mainD() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    std::string str;
    std::cin >> str;
    std::vector<char> a(26);
    for (int i = 0; i < 26; ++i)
        a[i] = (char) ('a' + i);
    for (int i = 0; i < str.size(); ++i) {
       // std::cout << a << std::endl;
        int j = std::distance(a.begin(),
                              std::find(a.begin(), a.end(), str[i]));
        std::cout << j + 1 << " ";
        char ch = a[j];
        std::vector<char> new_a;
        new_a.assign(a.begin(), a.end());
        for (int k = 0; k < j; ++k) {
            new_a[k + 1] = a[k];
        }
        new_a[0] =  ch;
        a = new_a;
    }
    std::cout << std::endl;
    return 0;
}