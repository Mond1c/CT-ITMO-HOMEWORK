#include <algorithm>
#include <iostream>
#include <array>
#include <numeric>

#include "otsuMethod.hpp"

std::array<uint64_t, 256> getHistogramm(image_type const & img, int threads_count) noexcept {
    std::array<uint64_t, 256> colors{};

    omp_set_dynamic(0);
    #pragma omp parallel if (threads_count != -1) num_threads(threads_count)
    {
        std::array<uint64_t, 256> colors_thread{};
        #pragma omp for nowait schedule(hs_kind, hs_chunk_size)
        for (int i = 0; i < img.size(); i++)
        {
            colors_thread[img[i]]++;
        }
        #pragma omp critical
        {
            for (int i = 0; i < 256; i++)
                colors[i] += colors_thread[i];
        }
    }
    return colors;
}

std::array<double, 256> getFrequenciesOfPixels(image_type const & img, const int threads_count) noexcept {
    auto hs = getHistogramm(img, threads_count); int size = img.size();
    std::array<double, 256> result{};

    for (int i = 0; i < 256; i++)
        result[i] = (double) hs[i] / size;
    return result; 
}

std::tuple<int, int, int> getBestThresholds(image_type const & img, int threads_count) noexcept {
    std::tuple<int, int, int> b_thresholds{};
    double b_sigma = -1; int size = img.size();

    std::array<double, 256> frequencies = getFrequenciesOfPixels(img, threads_count);
    std::array<double, 256> frequenciesPrefixSums, mues;
    frequenciesPrefixSums[0] = frequencies[0]; mues[0] = 0;
    for (int i = 1; i < 256; i++) {
        frequenciesPrefixSums[i] = frequenciesPrefixSums[i - 1] + frequencies[i];
        mues[i] = mues[i - 1] + i * frequencies[i];
    }


    omp_set_dynamic(0);
    #pragma omp parallel if (threads_count != -1) num_threads(threads_count) shared(frequenciesPrefixSums, mues)
    {
        double b_sigma_local = -1;
        std::tuple<int, int, int> b_thresholds_local{};
        #pragma omp for schedule(otsu_kind, otsu_chunk_size) nowait 
        for (int f0 = 0; f0 <= 253; f0++) {
            for (int f1 = f0 + 1; f1 <= 254; f1++) {
                for (int f2 = f1 + 1; f2 <= 255; f2++) {
                    double 
                        omega1 = frequenciesPrefixSums[f0],
                        omega2 = frequenciesPrefixSums[f1] - frequenciesPrefixSums[f0],
                        omega3 = frequenciesPrefixSums[f2] - frequenciesPrefixSums[f1],
                        omega4 = frequenciesPrefixSums[255] - frequenciesPrefixSums[f2],
                        mu1 = mues[f0],
                        mu2 = mues[f1] - mues[f0],
                        mu3 = mues[f2] - mues[f1],
                        mu4 = mues[255] - mues[f2];
                
                    double cur_sigma = mu1 * mu1 / omega1 + mu2 * mu2 / omega2 + mu3 * mu3 / omega3 + mu4 * mu4 / omega4;

                    if (b_sigma_local < cur_sigma) {
                        b_sigma_local = cur_sigma; 
                        b_thresholds_local = std::make_tuple(f0, f1, f2);
                    }
                }
            }
        }
        if (b_sigma < b_sigma_local) {
            #pragma omp critical
            {
                if (b_sigma < b_sigma_local) {
                    b_sigma = b_sigma_local; 
                    b_thresholds = b_thresholds_local;
                }
            }
        }
    }

    return b_thresholds;
}

image_type getFilteredImage(image_type const & image, const int f0, const int f1, const int f2, const int threads_count) {
    int size = image.size();
    image_type filteredImage(size);

    omp_set_dynamic(0);
    #pragma omp parallel for if (threads_count != -1) num_threads(threads_count) schedule(filter_kind, filter_chunk_size)
    for (int i = 0; i < size; i++) {
        uint8_t out;
        if      (image[i] <= f0) out = color0;
        else if (image[i] <= f1) out = color1;
        else if (image[i] <= f2) out = color2;
        else                     out = color3;
        filteredImage[i] = out;
    }
    return filteredImage;
}