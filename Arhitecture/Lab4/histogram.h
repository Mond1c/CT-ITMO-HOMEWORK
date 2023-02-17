//
// Created by pocht on 12.01.2023.
//

#ifndef OMP4_HISTOGRAM_H
#define OMP4_HISTOGRAM_H
#include <vector>
#include <cstdint>
#include <omp.h>

std::vector<int> generate_histogram(std::vector<uint8_t> &data, int width, int height, int threadCount, int chunkSize) {
    std::vector<int> histogram(256);
#pragma omp parallel for num_threads(threadCount) schedule(guided, chunkSize)
    for (int i = 0; i < width * height; ++i) {
#pragma omp atomic
        ++histogram[data[i]];
    }
    return histogram;
}

#endif //OMP4_HISTOGRAM_H
