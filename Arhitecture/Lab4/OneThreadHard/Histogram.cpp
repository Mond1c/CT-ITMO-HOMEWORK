//
// Created by mond1c on 1/4/23.
//

#include "Histogram.h"
#include <iostream>

std::map<int, int> Histogram::Generate(std::shared_ptr<utility::Image> &image) {
    std::map<int, int> histogram;
    for (int i = 0; i < image->GetHeight(); ++i) {
        for (int j = 0; j < image->GetWidth(); ++j) {
            ++histogram[(*image)[i][j]];
        }
    }
    return histogram;
}

void Histogram::Print(std::map<int, int> &histogram) {
    for (const auto& item : histogram) {
        std::cout << item.first << ": " << item.second << std::endl;
    }
}
