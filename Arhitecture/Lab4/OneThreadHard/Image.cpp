//
// Created by mond1c on 1/4/23.
//

#include "Image.h"
#include <omp.h>

void utility::Image::ReadFromFile() {
    std::string str;
    *file_ >> str;
    if (str != "P5") {
        throw std::runtime_error("Invalid file format, expected P5");
    }
    *file_ >> width_ >> height_;
    *file_ >> str;
    if (str != "255") {
        throw std::runtime_error("Invalid file format, expected 255");
    }
    colors_ = std::vector<std::vector<uint8_t>>(height_, std::vector<uint8_t>(width_));
    std::unique_ptr<char[]> buffer(new char[width_ * height_]);
    file_->read(buffer.get(), width_ * height_);
    for (int i = 0; i < height_; ++i) {
#pragma omp parallel for
        for (int j = 0; j < width_; ++j) {
            colors_[i][j] =(uint8_t) buffer[i * width_ + j];
        }
    }
}

int utility::Image::GetWidth() const {
    return width_;
}

int utility::Image::GetHeight() const {
    return height_;
}

uint8_t utility::Image::GetPixel(int y, int x) {
    return colors_[y][x];
}

std::vector<uint8_t> &utility::Image::operator[](int i) {
    return colors_[i];
}
