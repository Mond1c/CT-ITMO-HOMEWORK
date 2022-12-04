import random

MEM_SIZE = 2097152
CACHE_SIZE = 16384
CACHE_LINE_SIZE = 128
CACHE_LINE_COUNT = 128
CACHE_WAY = 2
CACHE_SETS_COUNT = 64
CACHE_TAG_SIZE = 8
CACHE_SET_SIZE = 6
CACHE_OFFSET_SIZE = 7
CACHE_ADDR_SIZE = 21

DATA1_BUS_SIZE = 16
DATA2_BUS_SIZE = 16
ADDR1_BUS_SIZE = 14
ADDR2_BUS_SIZE = 14
CTR1_BUS_SIZE = 3
CTR2_BUS_SIZE = 2

C1_NOP = 0
C1_READ8 = 1
C1_READ16 = 2
C1_READ32 = 3
C1_INVALIDATE_LINE = 4
C1_WRITE8 = 5
C1_WRITE16 = 6
C1_WRITE32 = 7
C1_RESPONSE = 7
C2_NOP = 0
C2_READ_LINE = 2
C2_WRITE_LINE = 2
C2_RESPONSE = 1


class Memory:
    def __init__(self, data=[[0] * CACHE_LINE_SIZE] * CACHE_SIZE):
        self.data = data
        self.tag = [0] * CACHE_SIZE
        for i in range(CACHE_SIZE):
            self.tag[i] = i

    def read_line(self, tag):
        for i in range(len(self.data)):
            if self.tag[i] == tag:
                return self.data[i]

    def write_line(self, tag, line):
        for i in range(len(self.data)):
            if self.tag[i] == tag:
                self.data[i] = line
                return


class Cache:
    def __init__(self, data=[[0] * CACHE_LINE_SIZE] * CACHE_SIZE):
        self.cache_hits = 0
        self.cache_misses = 0
        self.valid = [0] * CACHE_LINE_COUNT
        self.dirty = [0] * CACHE_LINE_COUNT
        self.data = [[0] * CACHE_LINE_SIZE] * CACHE_LINE_COUNT
        self.tag = [0] * CACHE_LINE_COUNT
        self.lru = [0] * CACHE_LINE_COUNT
        self.mem_ctr = Memory(data)

    def read(self, addr_set, addr_tag, addr_offset, data_size):
        '''if addr_set * CACHE_WAY >= len(self.tag):
            self.cache_misses += 1
            if self.lru[(addr_set * CACHE_WAY) % CACHE_LINE_COUNT] < self.lru[((addr_set * CACHE_WAY) % CACHE_LINE_COUNT) + 1]:
                index = (addr_set * CACHE_WAY) % CACHE_LINE_COUNT
                if self.dirty[index] == 1:
                    self.mem_ctr.write_line(self.tag[index], self.data[index])
                self.tag[index] = addr_tag
                self.valid[index] = 1
                self.data[index] = self.mem_ctr.read_line(addr_tag)
                return self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + data_size]
            else:
                self.cache_misses += 1
                index = ((addr_set * CACHE_WAY) % CACHE_LINE_COUNT) + 1
                if self.dirty[index] == 1:
                    self.mem_ctr.write_line(self.tag[index], self.data[index])
                self.tag[index] = addr_tag
                self.valid[index] = 1
                self.data[index] = self.mem_ctr.read_line(addr_tag)
                return self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + data_size]'''
        if self.tag[addr_set * CACHE_WAY] == addr_tag:
            self.cache_hits += 1
            return self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + data_size]
        elif self.tag[addr_set * CACHE_WAY + 1] == addr_tag:
            self.cache_hits += 1
            return self.data[addr_set * CACHE_WAY + 1][addr_offset:addr_offset + data_size]
        elif self.valid[addr_set * CACHE_WAY] == 0:
            self.cache_misses += 1
            self.valid[addr_set * CACHE_WAY] = 1
            self.tag[addr_set * CACHE_WAY] = addr_tag
            self.data[addr_set * CACHE_WAY] = self.mem_ctr.read_line(addr_tag)
            return self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + data_size]
        elif self.valid[addr_set * CACHE_WAY + 1] == 0:
            self.cache_misses += 1
            self.valid[addr_set * CACHE_WAY + 1] = 1
            self.tag[addr_set * CACHE_WAY + 1] = addr_tag
            self.data[addr_set * CACHE_WAY + 1] = self.mem_ctr.read_line(addr_tag)
            return self.data[addr_set * CACHE_WAY + 1][addr_offset:addr_offset + data_size]
        elif self.lru[addr_set * CACHE_WAY] < self.lru[addr_set * CACHE_WAY + 1]:
            self.cache_misses += 1
            if self.dirty[addr_set * CACHE_WAY] == 1:
                self.mem_ctr.write_line(self.tag[CACHE_WAY * addr_set], self.data[addr_set * CACHE_WAY])
            self.dirty[addr_set * CACHE_WAY] = 0
            self.tag[addr_set * CACHE_WAY] = addr_tag
            self.lru[addr_set * CACHE_WAY] = 1
            return self.data[CACHE_WAY * addr_set][addr_offset:addr_offset + data_size]
        else:
            self.cache_misses += 1
            if self.dirty[addr_set * CACHE_WAY + 1] == 1:
                self.mem_ctr.write_line(self.tag[CACHE_WAY * addr_set + 1], self.data[CACHE_WAY * addr_set + 1])
            self.dirty[addr_set * CACHE_WAY + 1] = 0
            self.tag[addr_set * CACHE_WAY + 1] = addr_tag
            self.lru[addr_set * CACHE_WAY + 1] = 1
            return self.data[addr_set * CACHE_WAY + 1][addr_offset:addr_offset + data_size]

    def write(self, addr_set, addr_tag, addr_offset, data):
        if self.tag[addr_set * CACHE_WAY] == addr_tag:
            self.cache_hits += 1
            self.dirty[addr_set * CACHE_WAY] = 1
            self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + len(data)] = data
        elif self.tag[addr_set * CACHE_WAY + 1] == addr_tag:
            self.cache_hits += 1
            self.dirty[addr_set * CACHE_WAY + 1] = 1
            self.data[addr_set * CACHE_WAY + 1][addr_offset:addr_offset + len(data)] = data
        elif self.valid[addr_set * CACHE_WAY] == 0:
            self.cache_misses += 1
            self.valid[addr_set * CACHE_WAY] = 1
            self.dirty[addr_set * CACHE_WAY] = 1
            self.tag[addr_set * CACHE_WAY] = addr_tag
            self.data[addr_set * CACHE_WAY] = self.mem_ctr.read_line(addr_tag)
            self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + len(data)] = data
        elif self.valid[addr_set * CACHE_WAY + 1] == 0:
            self.cache_misses += 1
            self.valid[addr_set * CACHE_WAY + 1] = 1
            self.dirty[addr_set * CACHE_WAY + 1] = 1
            self.tag[addr_set * CACHE_WAY + 1] = addr_tag
            self.data[addr_set * CACHE_WAY + 1] = self.mem_ctr.read_line(addr_tag)
            self.data[addr_set * CACHE_WAY + 1][addr_offset:addr_offset + len(data)] = data
        elif self.lru[addr_set * CACHE_WAY] < self.lru[addr_set * CACHE_WAY + 1]:
            self.cache_misses += 1
            if self.dirty[addr_set * CACHE_WAY] == 1:
                self.mem_ctr.write_line(self.tag[CACHE_WAY * addr_set], self.data[CACHE_WAY * addr_set])
            self.dirty[addr_set * CACHE_WAY] = 1
            self.tag[addr_set * CACHE_WAY] = addr_tag
            self.data[addr_set * CACHE_WAY] = self.mem_ctr.read_line(addr_tag)
            self.data[addr_set * CACHE_WAY][addr_offset:addr_offset + len(data)] = data
            self.lru[addr_set * CACHE_WAY] = 1
        else:
            self.cache_misses += 1
            if self.dirty[addr_set * CACHE_WAY + 1] == 1:
                self.mem_ctr.write_line(self.tag[CACHE_WAY * addr_set + 1], self.data[CACHE_WAY * addr_set + 1])
            self.dirty[addr_set * CACHE_WAY + 1] = 1
            self.tag[addr_set * CACHE_WAY + 1] = addr_tag
            self.data[addr_set * CACHE_WAY + 1] = self.mem_ctr.read_line(addr_tag)
            self.data[addr_set * CACHE_WAY + 1][addr_offset:addr_offset + len(data)] = data
            self.lru[addr_set * CACHE_WAY + 1] = 1

    def invalidate_line(self, addr_set, addr_tag):
        if self.tag[addr_set * CACHE_WAY] == addr_tag:
            if self.dirty[addr_set * CACHE_WAY] == 1:
                self.mem_ctr.write_line(addr_tag, self.data[addr_set * CACHE_WAY])
            self.valid[addr_set * CACHE_WAY] = 0
            self.dirty[addr_set * CACHE_WAY] = 0
            self.tag[addr_set * CACHE_WAY] = 0
            self.data[addr_set * CACHE_WAY] = 0
        elif self.tag[addr_set * CACHE_WAY + 1] == addr_tag:
            if self.dirty[addr_set * CACHE_WAY + 1] == 1:
                self.mem_ctr.write_line(addr_tag, self.data[addr_set * CACHE_WAY])
            self.valid[addr_set * CACHE_WAY] = 0
            self.dirty[addr_set * CACHE_WAY] = 0
            self.tag[addr_set * CACHE_WAY] = 0
            self.data[addr_set * CACHE_WAY] = 0

    def cache_info(self):
        requests = self.cache_hits + self.cache_misses
        print("Requsts =", requests)
        print("Cache hits =", self.cache_hits)
        print("Cache misses =", self.cache_misses)
        if requests != 0:
            print("Cache hit ratio =", self.cache_hits / requests)
            print("Cache miss ratio =", self.cache_misses / requests)


def main():
    SEED = 225526
    M = 64
    N = 60
    K = 32
    # a = [[0] * K] * M # 8 bits
    # b = [[0] * N] * K # 16 bits
    # c = [[0] * M] * N # 32 bits
    random.seed(SEED)
    # We need to write a, b, c in the main memory
    # And generate this arrays with random number
    data = [[0] * CACHE_LINE_SIZE] * CACHE_SIZE
    n = 0
    m = 0
    k = 0
    for i in range(M):
        for j in range(K):
            if k >= CACHE_LINE_SIZE:
                m += 1
                k = 0
            number = bin(random.randint(-128, 127) & 127)
            binary_form_of_number = list(map(int, number[2::]))  # int list equals binary form of random number
            for l in range(0, 8):
                if l < len(binary_form_of_number):
                    data[m][k] = binary_form_of_number[l]
                k += 1
    # a:    values in cache_line = 128 / 8 = 16, cache_lines_count = K * M / 16 = 128 lines
    #       tag = 0...128
    n = 0
    k = 0
    m = 0
    for i in range(K):
        for j in range(N):
            if n >= CACHE_LINE_SIZE:
                k += 1
                n = 0
            number = bin(random.randint(-32768, 32767) & 32767)
            binary_form_of_number = list(map(int, number[2::]))
            for l in range(0, 16):
                if l < len(binary_form_of_number):
                    data[129 + k][n] = binary_form_of_number[l]
                n += 1
    # b:     values in cache_line = 128 / 16 = 8, cache_lines_count = N * K / 8 = 240 lines
    #       tag = 129..369
    n = 0
    m = 0
    k = 0
    for i in range(M):
        for j in range(N):
            if n >= CACHE_LINE_SIZE:
                m += 1
                n = 0
            number = bin(random.randint(-2147483648, 2147483647) & 2147483647)
            binary_form_of_number = list(map(int, number[2::]))
            for l in range(0, 32):
                if l < len(binary_form_of_number):
                    data[370 + m][n] = binary_form_of_number[l]
                n += 1
    # c:     values in cache_line = 128 / 32 = 4, cache_lines_count = M * N / 4 = 960 lines
    #       tag = 370..1330

    # y, x, k, s, pa, pb, pc it's registers in cpu, you do not need to store it in the cache
    pa = 0
    pb = 0
    pc = 0
    s = 0

    # now we have data in the main memory
    # Create cache with initialization start memory (we write this data in the main memory not in cache)
    cache = Cache(data)
    # pa = 0
    # pc = 0
    a = 0
    b = 0
    c = 0
    for y in range(M):
        x = 0
        while x < N * 32:
            pb = 0
            s = 0
            k = 0
            m = 0
            while k < K * 8 and m < K * 16:
                #  print(k * 8)
                if a - pa * CACHE_LINE_SIZE >= 0:
                    pa += 1
                if b - pb * CACHE_LINE_SIZE >= 0:
                    pb += 1
                #print(k * 8)
                a_n = ''.join(str(i) for i in cache.read(pa % 64, pa, a % CACHE_LINE_SIZE, 8))
                b_n = ''.join(str(i) for i in cache.read(pb % 64, 129 + pb, b % CACHE_LINE_SIZE, 16))
                s += int(a_n, 2) + int(b_n, 2)
                k += 8
                m += 16
                a += 8
                b += 16
            data = [0] * 32
            binary_form_of_number = list(map(int, bin(s & 2147483647)[2::]))
            for l in range(32):
                if l < len(binary_form_of_number):
                    data[l] = binary_form_of_number[l]
            if c - pc * CACHE_LINE_SIZE >= 0:
                pc += 1
            cache.write(pc % 64, 370 + pc, c % CACHE_LINE_SIZE, data)
            x += 32
            c += 32
    cache.cache_info()


if __name__ == "__main__":
    main()
