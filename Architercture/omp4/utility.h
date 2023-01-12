//
// Created by pocht on 12.01.2023.
//

#ifndef OMP4_UTILITY_H
#define OMP4_UTILITY_H

#include <vector>
#include <fstream>
#include <memory>

struct image_size {
    int width;
    int height;
};

std::pair<image_size, std::vector<uint8_t>> read_from_file(const std::string &fileName) {
    std::ifstream file(fileName);
    std::vector<uint8_t> data;
    int width;
    int height;
    std::string str;
    file >> str;
    if (str != "P5") {
        throw std::runtime_error("Invalid file format, expected P5");
    }
    file >> width >> height;
    file >> str;
    if (str != "255") {
        throw std::runtime_error("Invalid file format, expected 255");
    }
    data.resize(width * height);
    std::unique_ptr<char[]> buffer(new char[height * width]);
    file.read(buffer.get(), width * height);
    for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
            data[i * width + j] = (uint8_t) buffer[i *  width + j];
        }
    }
    file.close();
    return {image_size{width, height}, data};
}

void write_into_file(const std::string &fileName, int width, int height, std::vector<char> &data) {
    std::ofstream file(fileName);
    file << "P5" << std::endl;
    file << width << " " << height << std::endl;
    file << 255 << std::endl;
    file.write(data.data(), data.size());
    file.close();
}

template<typename... Args>
std::string get_format_string(const std::string &format, Args... args) {
    std::size_t size = std::snprintf(nullptr, 0, format.c_str(), args ...) + 1;
    std::unique_ptr<char[]> buffer(new char[size]);
    std::snprintf(buffer.get(), size, format.c_str(), args ...);
    return {buffer.get(), buffer.get() + size - 1};
}

#endif //OMP4_UTILITY_H
