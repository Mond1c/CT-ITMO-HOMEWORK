import os
import subprocess
import time

FILE_FOR_EXECUTE_NAME = 'run.bat'
EXECUTABLE_FILE_NAME = 'omp4.exe'
INPUT_FILE_NAME = '../in.pgm'
OUTPUT_FILE_NAME = '../output.pgm'
OUTPUT = open('output.dat', 'w')
TEST_COUNT = 1
MIN_THREADS = 1
MAX_THREADS = 20
MIN_CHUNK_SIZE = 1
MAX_CHUNK_SIZE = 8
THREADS_FOR_CHUNK_SIZE_TEST = 20


def test():
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
                sum_time += int(line.split(':')[1].split('ms')[0].strip())
        print("TEST", i + 1, "PASSED!")
        time.sleep(10) # This line need to solve a problem with throttling
    OUTPUT.write('AVERAGE TIME: ' + str(sum_time / TEST_COUNT) + ' ms\n')

def main():
    # Different thread test
    OUTPUT.write("======TEST DIFFERENT THREADS COUNT======\n")
    for i in range(MIN_THREADS, MAX_THREADS+1):
        with open(FILE_FOR_EXECUTE_NAME, 'w') as file:
            file.write('{} {} {} {}'.format(EXECUTABLE_FILE_NAME, i, INPUT_FILE_NAME, OUTPUT_FILE_NAME))
        print("RUN TESTS WITH {} THREADS".format(i))
        OUTPUT.write("========================\n")
        OUTPUT.write("RUN TESTS WITH {} THREADS\n".format(i))
        test()
    OUTPUT.write("======TEST DIFFERENT CHUNK_SIZE======\n")
    for i in range(MIN_CHUNK_SIZE, MAX_CHUNK_SIZE+1):
        with open(FILE_FOR_EXECUTE_NAME, 'w') as file:
            file.write('{} {} {} {} {}'.format(EXECUTABLE_FILE_NAME, THREADS_FOR_CHUNK_SIZE_TEST, INPUT_FILE_NAME, OUTPUT_FILE_NAME, i))
        print("RUN TESTS WITH CHUNK_SIZE = {}".format(i))
        OUTPUT.write("========================\n")
        OUTPUT.write("RUN TESTS WITH CHUNK_SIZE = {}\n".format(i))
        test()

main()
OUTPUT.close()
