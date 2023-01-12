//
// Created by pocht on 12.01.2023.
//

#ifndef OMP4_OTSU_H
#define OMP4_OTSU_H
#include <vector>
#include <omp.h>
#include <cstdint>
#include <iostream>
#include "utility.h"
#include "histogram.h"

std::vector<double> precalculate_chance_for_thresholds(std::vector<int> &histogram, int width, int height) {
    double pixelCount = width * height;
    std::vector<double> q(256);
    q[0] = histogram[0] / pixelCount;
    for (int i = 1; i < 256; ++i) {
        q[i] = q[i - 1] + (histogram[i] / pixelCount);
    }
    return q;
}

std::vector<double> precalculate_average(std::vector<int> &histogram, int width, int height) {
    double pixelCount = width * height;
    std::vector<double> u(256);
    u[0] = 0;
    for (int i = 1; i < 256; ++i) {
        u[i] = u[i - 1] + (i * histogram[i] / pixelCount);
    }
    return u;
}


struct Thresholds {
    uint8_t f0;
    uint8_t f1;
    uint8_t f2;
};

Thresholds calculate_thresholds(std::vector<int> &histogram, int width, int height, int threadCount, int chunkSize) {
    std::vector<double> q = precalculate_chance_for_thresholds(histogram, width, height);
    std::vector<double> u = precalculate_average(histogram, width, height);
    double v_max = 0;
    Thresholds answer;
#pragma omp parallel for if (threadCount != -1) num_threads(threadCount) schedule(dynamic, chunkSize) default(none) shared(answer, v_max, q, u, chunkSize)
    for (int f0 = 0; f0 < 254; ++f0) {
        for (int f1 = f0 + 1; f1 < 255; ++f1) {
            for (int f2 = f1 + 1; f2 < 256; ++f2) {
                double q0 = q[f0] - q[0];
                double q1 = q[f1] - q[f0];
                double q2 = q[f2] - q[f1];
                double q3 = q[255] - q[f2];
                double u0 = (u[f0] - u[0]) / q0;
                double u1 = (u[f1] - u[f0]) / q1;
                double u2 = (u[f2] - u[f1]) / q2;
                double u3 = (u[255] - u[f2]) / q3;
                double u = q0 * u0 + q1 * u1 + q2 * u2 + q3 * u3;
                double v = q0 * (u0 - u) * (u0 - u) + q1 * (u1 - u) * (u1 - u)
                    + q2 * (u2 - u) * (u2 - u) + q3 * (u3 - u) * (u3 - u);
                if (v > v_max) {
#pragma omp critical
                    if (v > v_max) {
                        answer = Thresholds{(uint8_t)f0,(uint8_t) f1, (uint8_t)f2};
                        v_max = v;
                    }
                }
            }
        }
    }
    return answer;
}

std::vector<char> get_new_image(std::vector<uint8_t> &data, Thresholds t, int threadCount, int chunkSize) {
    std::vector<char> new_image(data.size());
    omp_set_dynamic(0);
#pragma omp parallel for if (threadCount != -1) num_threads(threadCount)  shared(new_image) schedule(static, chunkSize)
    for (int i = 0; i < data.size(); ++i) {
        char ch;
        if (data[i] <= t.f0) {
            ch = (char)0;
        } else if (data[i] <= t.f1) {
            ch = (char)84;
        } else if (data[i] <= t.f2) {
            ch = (char)170;
        } else {
            ch = (char)255;
        }
        new_image[i] = ch;
    }
    return new_image;
}


std::vector<char> generate(std::vector<uint8_t> &data, int width, int height, int threadCount, int chunkSize) {
    std::vector<int> histogram = generate_histogram(data, width, height, threadCount, chunkSize);
    Thresholds t = calculate_thresholds(histogram, width, height, threadCount, chunkSize);
    std::cout << get_format_string("%u %u %u\n", t.f0, t.f1, t.f2);
    return get_new_image(data, t, threadCount, chunkSize);
}


#endif //OMP4_OTSU_H
