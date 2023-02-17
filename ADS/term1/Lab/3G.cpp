#include <bits/stdc++.h>

int main() {
    int t;
    std::cin >> t;
    while (t--) {
        int round = 0;
        int crewmates, impostors;
        std::cin >> impostors >> crewmates;
        while (crewmates > 0 && impostors > 0) {
            crewmates -= impostors;
            impostors--;
            round++;
        }
        int left = 0, right = crewmates + impostors;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (impostors - mid < 0) {
                right = mid - 1;
            } else if (impostors - mid > 0) {
                left = mid + 1;
            } else {
                right = mid;
                break;
            }
        }

        if (impostors > 0) {
            std::cout << "Impostors" << std::endl;
        } else {
            std::cout << "Crewmates" << std::endl;
        }
        std::cout << round << std::endl;
    }
    return 0;
}