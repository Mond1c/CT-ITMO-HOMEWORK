from functools import lru_cache

M = 64
N = 60
K = 32

a = [[] * K] * M
b = [[] * N] * K
c = [[] * M] * N

@lru_cache(maxsize=128)
def mmul():
	pa = 0
	pc = 0
	for y in range(0, M):
		for x in range(0, N):
			pb = 0
			s = 0
			for k in range(0, K):
				s += a[pa + k] * b[pb + x]
				pb += N
			c[pc + x] = s
	pa += K
	oc += N

mmul()
mmul.cache_info()