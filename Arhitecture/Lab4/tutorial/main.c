#include <stdio.h>
#include <omp.h>
int main() {
    int k = 0;
#pragma omp parallel
    {
        int kn = 0;
#pragma omp for
        for (long i = 0; i < 3000000000; i++) {
           // printf("%i (%i)\n", i, omp_get_thread_num());
//#pragma omp atomic //This is first variant to solve this problem
  //          k += i;
/*#pragma omp critical // This is second variant to solve this problem
            {
                k += i;
            }
            */
            kn += i;
        }
#pragma omp atomic // It's working without this line, but sometimes you can get a wrong answer
    k += kn;
    }
    printf("%i\n", k);
    return 0;
}
