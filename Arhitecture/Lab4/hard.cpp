#include <iostream>
#include "otsu.h"

int main(int argc, char* argv[]) {
    if (argc != 4 && argc != 5) {
        throw std::runtime_error("invalid count of command line arguments");
    }
    int chunkSize = 1;
    if (argc == 5) {
        chunkSize = std::stoi(argv[4]);
    }
    int threadCount = std::stoi(argv[1]);
    std::string inputFileName = argv[2];
    std::string outputFileName = argv[3];
    auto p = read_from_file(inputFileName);
    double start = omp_get_wtime();
    std::vector<char> data = generate(p.second, p.first.width, p.first.height, threadCount, chunkSize);
    double end = omp_get_wtime();
    write_into_file(outputFileName, p.first.width, p.first.height, data);
    std::cout << get_format_string("Time (%i thread(s)): %g ms\n", threadCount, (end - start) * 1000);
    return 0;
}
