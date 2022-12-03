#include <iostream>

int main() {
    long sum = 0;
    for (long i = 0; i < 1000000000; i++) {
        sum = sum + i;
    }
    std::cout << sum << std::endl;
    return 0;
}
