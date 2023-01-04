//
// Created by mond1c on 1/4/23.
//

#ifndef ONETHREADHARD_IMAGE_H
#define ONETHREADHARD_IMAGE_H
#include <string>
#include <fstream>
#include <memory>
#include <vector>
#include <stdexcept>
#include <cstdint>

#include <iostream>

namespace utility {
    class Image {
    private:
        std::unique_ptr<std::ifstream> file_;
        std::vector<std::vector<uint8_t>> colors_;
        int width_{};
        int height_{};
    public:
        explicit Image(const std::string& fileName)
            : file_(std::make_unique<std::ifstream>(fileName, std::ifstream::binary)) {
            if (!(*file_)) {
                throw std::runtime_error("Can't open file " + fileName);
            }
            ReadFromFile();
        }

        Image(Image&) = delete;
        Image(Image&&) = delete;

        Image& operator=(Image&) = delete;
        Image& operator=(Image&&) = delete;

        ~Image() {
            file_->close();
        }
    public:
        [[nodiscard]] int GetWidth() const;
        [[nodiscard]] int GetHeight() const;
        uint8_t GetPixel(int y, int x);
    public:
        std::vector<uint8_t>& operator[](int i);
    private:
        void ReadFromFile();
    };
}

#endif //ONETHREADHARD_IMAGE_H
