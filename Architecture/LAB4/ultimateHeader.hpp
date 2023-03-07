#pragma once
#include <vector>
#include <cstdint>
#include <tuple>
#include "omp.h"

using image_type = std::vector<uint8_t>;

std::tuple<int, int, image_type> read_pgm(char * filename);
std::tuple<int, int, int> getBestThresholds(const int size, image_type const & img, int threads_count) noexcept;
void writeImageWithThresholds(int f0, int f1, int f2, int height, int width, image_type const & image, const char * filename);

constexpr uint8_t color0 = 0, color1 = 84, color2 = 170, color3 = 255;