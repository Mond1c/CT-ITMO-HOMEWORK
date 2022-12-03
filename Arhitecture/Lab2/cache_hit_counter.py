from functools import lru_cache

M = 64
N = 60
K = 32

a = [[0] * K] * M
b = [[0] * N] * K
c = [[0] * N] * M
s = 0
pa = 0
pb = 0
pc = 0
cache_miss_count = 0
cache_size = 16

@lru_cache(maxsize=8)
def get_from_a(pa, k):
    global cache_miss_count
    cache_miss_count += 1
    return a[pa][k]


@lru_cache(maxsize=16)
def get_from_b(pb, x):
    global cache_miss_count
    cache_miss_count += 1
    return b[pb][x]


@lru_cache(maxsize=32)
def get_s():
    global cache_miss_count
    cache_miss_count += 1
    return s


@lru_cache(maxsize=32)
def write_in_s(value):
    global cache_miss_count, s
    cache_miss_count += 1
    s = value


@lru_cache(maxsize=32)
def write_in_c(pc, x, value):
    global cache_miss_count
    cache_miss_count += 1
    c[pc][x] = value


@lru_cache(maxsize=8)
def write_in_pa(value):
    global cache_miss_count, pa
    cache_miss_count += 1
    pa = value


@lru_cache(maxsize=16)
def write_in_pb(value):
    global cache_miss_count, pb
    cache_miss_count += 1
    pb = value


@lru_cache(maxsize=32)
def write_in_pc(value):
    global cache_miss_count, pc
    cache_miss_count += 1
    pc = value


@lru_cache(maxsize=8)
def get_pa():
    global cache_miss_count
    cache_miss_count += 1
    return pa


@lru_cache(maxsize=16)
def get_pb():
    global cache_miss_count
    cache_miss_count += 1
    return pb


@lru_cache(maxsize=32)
def get_pc():
    global cache_miss_count
    cache_miss_count += 1
    return pc


@lru_cache(maxsize=32)
def get_x(x):
    global cache_miss_count
    cache_miss_count += 1
    return x


@lru_cache(maxsize=32)
def get_k(k):
    global cache_miss_count
    cache_miss_count += 1
    return k

@lru_cache(maxsize=32)
def get_y(y):
    global cache_miss_count
    cache_miss_count += 1
    return y


count = 0

def mmul():
    global count
    write_in_pa(0)
    write_in_pb(0)
    count += 2
    for y in range(M):
        get_y(y)
        get_y(y) # call get_y() two times because y = y + 1 we need to read y and write new value in y that equals two calls
        for x in range(N):
            get_x(x)
            get_x(x)
            write_in_pb(0)
            write_in_s(0)
            count += 2
            for k in range(K):
                get_k(k)
                get_k(k)
                # print(pa, k)
                write_in_s(get_s() + get_from_a(get_pa(), get_k(k)) * get_from_b(get_pb(), get_x(x)))
                write_in_pb(get_pb() + 1)
                count += 10
            write_in_c(get_pc(), get_x(x), get_s())
            count += 4
        write_in_pa(get_pa() + 1)
        write_in_pc(get_pc() + 1)
        count += 4


mmul()
print("Cache misses =", cache_miss_count)
print("Cache count = ", count)
print(get_x.cache_info())
print("Cache hit ratio = ", (count - cache_miss_count) / count)
