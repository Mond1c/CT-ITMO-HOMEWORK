import enum

HITS, MISSES, TICKS = 0, 0, 0

# 3 var
class constants(enum.IntEnum):
    MEM_SIZE = 2 ** 18
    CACHE_SIZE = 2 ** 11 
    CACHE_LINE_SIZE = 2 ** 4 
    CACHE_LINE_COUNT = 2 ** 7
    CACHE_WAY = 2
    CACHE_SETS_COUNT = 2 ** 6
    CACHE_TAG_SIZE = 8
    CACHE_SET_SIZE = 6
    CACHE_OFFSET_SIZE = 4
    CACHE_ADDR_SIZE = 18

class CacheLine:
    Valid = bool; Dirty = bool; Tag = int

    def __init__(self) -> None:
        pass

    def __init__(self, Valid = 0, Dirty = 0, Tag = 0):
        self.Valid = Valid
        self.Dirty = Dirty
        self.Tag = Tag

class Cache:
    lines = list[list[CacheLine]]
    displace = list[bool]
    
    def __init__(self) -> None:
        self.lines = [[CacheLine() for i in range(constants.CACHE_WAY)] for j in range(constants.CACHE_SETS_COUNT)]
        self.displace = [0 for i in range(constants.CACHE_SETS_COUNT)]

    def readOrWrite(self, address, dirtyBit): 
        global HITS, MISSES, TICKS
        tag = getTag(address)
        set = getSet(address)

        for i in range(constants.CACHE_WAY): 
            if self.lines[set][i].Tag == tag and self.lines[set][i].Valid == True: # CACHE HIT
                HITS += 1
                TICKS += 6 # CACHE_HIT_TIME
                if dirtyBit:                self.lines[set][i].Dirty = 1
                self.displace[set] = 1 - i
                return

        TICKS += 4  # CACHE_MISS_TIME
        MISSES += 1
        for i in range(constants.CACHE_WAY):
            if self.lines[set][i].Valid == False: 
                TICKS += 100 + constants.CACHE_LINE_SIZE // 2   # C2_READLINE
                self.lines[set][i] = CacheLine(1, dirtyBit, tag)
                self.displace[set] = 1 - i
                return
        
        if self.lines[set][self.displace[set]].Dirty == 1: # LRU-politics
            TICKS += 101 #write to memory
        TICKS += 100 + constants.CACHE_LINE_SIZE // 2           #read from memory
        self.lines[set][self.displace[set]] = CacheLine(1, dirtyBit, tag)
        self.displace[set] = 1 - self.displace[set]
        return

def getTag(address):
    return address >> (constants.CACHE_OFFSET_SIZE + constants.CACHE_SET_SIZE)

def getSet(address):
    return (address >> (constants.CACHE_OFFSET_SIZE)) % (1 << constants.CACHE_SET_SIZE)

def getOffset(address):
    return address % (1 << constants.CACHE_OFFSET_SIZE)

# ====MAIN==== #
cache = Cache();

M, N, K = 64, 60, 32

start_array_a = 0
start_array_b = start_array_a + M * K
start_array_c = start_array_b + K * N * 2 # учитываем, что массив b состоял из int16

pa = start_array_a;  TICKS += 1
pc = start_array_c;  TICKS += 1

TICKS += 1 # y = 0
for y in range(M):
    TICKS += 1 # y < M
    TICKS += 1 # x = 0
    for x in range(N):
        TICKS += 1 # X < N
        pb = start_array_b;     TICKS += 1
        TICKS += 1 # s = 0
        TICKS += 1 # k = 0
        for k in range(K): 
            TICKS += 1 # k < K
            cache.readOrWrite(pa + k, False) # C1_READ8
            TICKS += 1 # 1 такт на возврат данных
            cache.readOrWrite(pb + x * 2, False) # C1_READ16
            TICKS += 1 # 1 такт на возврат данных
            TICKS += 5 + 1 # умножение + сложение
            pb += N * 2;        TICKS += 1
            TICKS += 1 # k++
        cache.readOrWrite(pc + x * 4, True) # C1_WRITE32
        TICKS += 1 # 1 такт на возврат 
        TICKS += 1 # x++
    pa += K;      TICKS += 1
    pc += N * 4;  TICKS += 1
    TICKS += 1 # y++

TICKS += 1 # выход из функции
# ====MAIN==== #

print(f"HITS: {HITS} MISSES: {MISSES} TOTAL: {HITS + MISSES} TICKS: {TICKS}")
