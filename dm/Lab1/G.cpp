#include <iostream>
#include <vector>
#include <algorithm>
#include <sstream>

int main() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    int n;
    std::cin >> n;
    std::vector<unsigned long long> a(n);
    unsigned long long s;
    for (int i = 0; i < n; ++i)
        std::cin >> a[i];
    std::cin >> s;
    if (s == 0) {
        std::cout << "1&~1" << std::endl;
        return 0;
    }
    if (std::count(a.begin(), a.end(), s)) {
        std::cout << std::distance(a.begin(), std::find(a.begin(), a.end(), s)) + 1 << std::endl;
        return 0;
    }
    // [1, 0, 0, 0] = 8 = s
    // len = 4
    // one_count = 1 (count of one)
    // СДНФ 
    // build sdnf
    int len = 0;
    int one_count = 0;
    {
        unsigned long long k = s;
        while (k) {
            len++;
            one_count += (k % 2);
            k /= 2;
        }
    } 
    int k = 0;
    std::vector<std::vector<unsigned long long>> sdnf(one_count, std::vector<unsigned long long>(n));
    for (int i = 0; i < len; ++i) {
        bool s_bit = (s & (1 << (len - i - 1))) != 0;
        if (!s_bit) continue;
        for (int j = 0; j < n; ++j) {
            sdnf[k][j] = ((a[j] & (1 << (len - i - 1))) != 0);
            //std::cout << sdnf[k][j] << std::endl;
        }
        ++k;
    }
    // Now build answer string and check that answer equals s 
    std::stringstream ss;
    unsigned long long ans = 0;
   // std::cout << "Len: " << len << std::endl;
    for (int i = 0; i < one_count; ++i) {
        unsigned long long value = 0;
        if (i != 0) ss << "|(";
        else ss << "(";
        for (int j = 0; j < n; ++j) {
          //  std::cout << "J" << j << ": " << sdnf[i][j] << std::endl;
            if (j != 0) ss << "&";
            if (sdnf[i][j]) {
                if (j == 0) {
                    value = a[j];
                }
                else value = value & a[j];
                ss << j + 1;
                continue;
            }
            if (j == 0) value = ~a[j];
            else value = value & (~a[j]);
            ss << "~" << j + 1;
        }
        ss << ")";
        ans = ans | value;
    }
  //  std::cout << ans << " " << s << " " << ss.str() << std::endl;
    if (s != ans) {
        std::cout << "Impossible" << std::endl;
    } else {
        std::cout << ss.str() << std::endl;
    }
    return 0;
}