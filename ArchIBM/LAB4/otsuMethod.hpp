#pragma once
#include <vector>
#include <cstdint>
#include <tuple>
#include "omp.h"

using image_type = std::vector<uint8_t>;


std::tuple<int, int, image_type> read_pgm(const char * filename);
std::tuple<int, int, int> getBestThresholds(image_type const & img, int threads_count) noexcept;
void writeImage(int height, int width, image_type const & image, const char * filename);
image_type getFilteredImage(image_type const & image, const int f0, const int f1, const int f2, const int threads);

std::array<uint64_t, 256> getHistogramm(image_type const & img, int threads_count) noexcept;

const uint8_t color0 = 0, color1 = 84, color2 = 170, color3 = 255;

#define hs_kind dynamic
#define hs_chunk_size 32768
#define otsu_kind dynamic
#define otsu_chunk_size 1
#define filter_kind dynamic
#define filter_chunk_size 32768
