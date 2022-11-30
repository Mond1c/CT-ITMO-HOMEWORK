#include <stdint.h>

#define M 64
#define N 60
#define K 32
int8_t a[M][K];
int16_t b[K][N];
int32_t c[M][N];
void mmul() {
  int8_t *pa = &a;
  int32_t *pc = &c;
  for (int y = 0; y < M; y++)
  {
    for (int x = 0; x < N; x++)
    {
      int16_t *pb = &b;
      int32_t s = 0;
      for (int k = 0; k < K; k++)
      {
        s += pa[k] * pb[x];
        pb += N;
      }
    pc[x] = s;
  }
  pa += K;
  pc += N;
 }
}

int main() {
  mmul();
}