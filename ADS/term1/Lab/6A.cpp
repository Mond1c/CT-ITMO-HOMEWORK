#include <bits/stdc++.h>

int main() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    int n;
    std::cin >> n;
    std::deque<int> a, b;
    int customer = 1;
    std::string requests;
    std::cin >> requests;
    for (int i = 0; i < n; ++i) {
        switch (requests[i])
        {
        case 'a':
            a.push_back(customer++);
            break;
        case 'b':
            b.push_back(customer++);
            break;
        case 'A':
            std::cout << a.front();
            a.pop_front();
            break;
        case 'B':
            std::cout << b.front();
            b.pop_front();
            break;
        case '>':
            while (!a.empty()) {
                b.push_back(a.back());
                a.pop_back();
            }
            break;
        case ']':
            while (!b.empty()) {
                a.push_back(b.back());
                b.pop_back();
            }
            break;
        case '<':
            while (a.size() + 1 < b.size()) {
                a.push_back(b.back());
                b.pop_back();
            }
            break;
        case '[':
            while (b.size() + 1 < a.size()) {
                b.push_back(a.back());
                a.pop_back();
            }
            break;
        }
    }
    return 0;
}
