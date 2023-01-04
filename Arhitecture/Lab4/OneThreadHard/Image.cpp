//
// Created by mond1c on 1/4/23.
//

#include "Image.h"

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
    int i = 0, j = 0;
    int count = 0;
    while (*file_) {
        *file_ >> str;
        count += str.size();
        for (char ch : str) {
            if (j == width_) {
                j = 0;
                ++i;
            }
            colors_[i][j++] = 0xFF & ch;
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
