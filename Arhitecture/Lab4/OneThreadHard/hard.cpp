#include "Otsu.h"
#include <chrono>

int main(int argc, char **argv) {
    if (argc != 4 && argc != 1) {
        throw std::runtime_error("Invalid count of program arguments");
    }
    auto startTime = std::chrono::high_resolution_clock::now();
    Otsu otsu("../baboon.pgm");
    otsu.Generate();
    auto endTime = std::chrono::high_resolution_clock::now();
    auto time = endTime - startTime;
    std::cout << "Time: " << time / std::chrono::milliseconds(1) << "ms" << std::endl;
    return 0;
}
