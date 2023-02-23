#include <iostream>
#include <fstream>
#include <string>
#include <array>


#include "otsuMethod.hpp"

int main(int argc, char * argv[]) {
    if (argc != 4) {
        std::cerr << "Invalid count of arguments: " << argc - 1 << "\nExpected: 3 (count of threads, input file, output file)";
        return -1;
    }

    int threads = -1;
    try {
        threads = std::stoi(std::string(argv[1]));
    } catch (std::exception & error) {
        std::cerr << "Couldn't parse '" + std::string(argv[1]) + "' as number.";
        return -1;
    }

    if (threads == 0)   threads = omp_get_max_threads();
    
    int height, width;
    image_type image;

    try {
        const auto [h, w, im] = read_pgm(argv[2]);
        height = h; width = w; image = image_type(std::move(im));
    } catch (std::exception & error) {
        std::cerr << "Exception happened while reading file: " << error.what() << '\n';
        return -1;
    }

    double startTime = omp_get_wtime();
    const auto [f0, f1, f2] = getBestThresholds(image, threads);
    image_type output = getFilteredImage(image, f0, f1, f2, threads);
    double stopTime = omp_get_wtime();

    std::printf("Time (%i thread(s)): %g ms\n", (threads == -1 ? 1 : threads), (stopTime - startTime) * 1000);

    try {
        writeImage(height, width, output, argv[3]);
    } catch (std::exception & error) {
        std::cerr << "Exception happened while writing file: " << error.what() << '\n';
    }

    std::printf("%u %u %u\n", f0, f1, f2);
}