import os
import subprocess
import time
from openpyxl import Workbook

FILE_FOR_EXECUTE_NAME = 'run.bat'
EXECUTABLE_FILE_NAME = 'omp4.exe'
INPUT_FILE_NAME = '../in.pgm'
OUTPUT_FILE_NAME = '../output.pgm'
OUTPUT = open('output.dat', 'w')
TEST_COUNT = 5
MIN_THREADS = 1
MAX_THREADS = 20
MIN_CHUNK_SIZE = 1
MAX_CHUNK_SIZE = 64
THREADS_FOR_CHUNK_SIZE_TEST = 20

WORKBOOK = Workbook()
SHEET = WORKBOOK.active

ROW = 1
COLUMN = 'A'

def test():
    global ROW, COLUMN, SHEET, OUTPUT
    sum_time = 0
    for i in range(TEST_COUNT):
        OUTPUT.write("TEST " + str(i + 1) + ":\n")
        p = subprocess.Popen([FILE_FOR_EXECUTE_NAME], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        output, err = p.communicate()
        s = output.decode('utf-8').split('\n')[2::]
        for line in s:
            if len(line) > 0:
                OUTPUT.write(line)
            if line.startswith('Time'):
                cur_t = float(line.split(':')[1].split('ms')[0].strip())
                sum_time += cur_t
                SHEET[COLUMN + str(ROW)] = cur_t
                COLUMN = chr(ord(COLUMN) + 1)
        print("TEST", i + 1, "PASSED!")
        #time.sleep(10) # This line need to solve a problem with throttling
    OUTPUT.write('AVERAGE TIME: ' + str(sum_time / TEST_COUNT) + ' ms\n')
    return sum_time / TEST_COUNT

def main():
    global ROW, COLUMN, SHEET, OUTPUT
    # Different thread test
    SHEET['A1'] = 'Thread count'
    for i in range(TEST_COUNT):
        SHEET[chr(ord('A') + i + 1) + '1'] = 'Test ' + str(i + 1)
    SHEET[chr(ord('A') + TEST_COUNT + 1) + '1'] = 'Average'
    OUTPUT.write("======TEST DIFFERENT THREADS COUNT======\n")
    for i in range(MIN_THREADS, MAX_THREADS+1):
        with open(FILE_FOR_EXECUTE_NAME, 'w') as file:
            file.write('{} {} {} {}'.format(EXECUTABLE_FILE_NAME, i, INPUT_FILE_NAME, OUTPUT_FILE_NAME))
        print("RUN TESTS WITH {} THREADS".format(i))
        OUTPUT.write("========================\n")
        OUTPUT.write("RUN TESTS WITH {} THREADS\n".format(i))
        ROW += 1
        COLUMN = 'B'
        SHEET['A' + str(ROW)] = i
        average = test()
        SHEET[COLUMN + str(ROW)] = average
    OUTPUT.write("======TEST DIFFERENT CHUNK_SIZE======\n")
    best_chunk_size = 0
    best_time = 100000000
    ROW = MAX_THREADS + 10
    COLUMN = 'A'
    SHEET['A' + str(ROW)] = 'Chunk size'
    for i in range(TEST_COUNT):
        SHEET[chr(ord('A') + i + 1) + str(ROW)] = 'Test ' + str(i + 1)
    SHEET[chr(ord('A') + TEST_COUNT + 1) + str(ROW)] = 'Average'
    for i in range(MIN_CHUNK_SIZE, MAX_CHUNK_SIZE+1):
        with open(FILE_FOR_EXECUTE_NAME, 'w') as file:
            file.write('{} {} {} {} {}'.format(EXECUTABLE_FILE_NAME, THREADS_FOR_CHUNK_SIZE_TEST, INPUT_FILE_NAME, OUTPUT_FILE_NAME, i))
        print("RUN TESTS WITH CHUNK_SIZE = {}".format(i))
        OUTPUT.write("========================\n")
        OUTPUT.write("RUN TESTS WITH CHUNK_SIZE = {}\n".format(i))
        ROW += 1
        COLUMN = 'B'
        SHEET['A' + str(ROW)] = i
        average = test()
        SHEET[COLUMN + str(ROW)] = average
        if average < best_time:
            best_chunk_size = i
            best_time = average
    print("!!!Best chunk size is ", best_chunk_size, "!!!")

main()
OUTPUT.close()
WORKBOOK.save('data_dynamic.xlsx')