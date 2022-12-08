#include <iostream>

int main() {
    register long long i;
    long long sum = 0;
    for (i = 0; i < 1000000000; i++) {
        sum = sum + i;
    }
    std::cout << sum << std::endl;
    return 0;
}
