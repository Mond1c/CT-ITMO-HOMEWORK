#include <iostream>
#include <omp.h>

int main() {
    int k = 0;
    #pragma omp parallel
    {
        int kn = 0;
        #pragma omp for
        for (long i = 0; i < 3000000000; i++) {
            kn += i;
        }
        #pragma omp atomic
        k += kn;
    }
    std::cout << k << std::endl;
    return 0;
}