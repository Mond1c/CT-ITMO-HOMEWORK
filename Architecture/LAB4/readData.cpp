#include <iostream>
#include <fstream>
#include <exception>
#include <vector>
#include <tuple>
#include <sstream>
#include <string>

#include "otsuMethod.hpp"


std::tuple<int, int, image_type> read_pgm(const char * filename) { 
    std::ifstream file(filename);
    if (!file.good()) {
        throw std::runtime_error("Couldn't open file");
    }

    std::string p5;
    std::getline(file, p5);

    if (p5 != "P5") {
        throw std::runtime_error("Bad input file\n: First line: \"" + p5 + "\"; Expected: \"P5\"");
    }

    int height, width; file >> height >> width;
    if (!file.good()) {
        throw std::runtime_error("Couldn't read width & height of image");
    }

    int mustbe255; file >> mustbe255;
    if (!file.good() || mustbe255 != 255) {
        throw std::runtime_error("Third string must be 255");
    }

    image_type image(width * height, 0);
    std::getline(file, p5);

    for (int i = 0; i < width * height; i++) {
        char ch; file.read(&ch, 1);
        image[i] = ch;
    }

    if (!file.good()) { throw std::runtime_error("IOException while reading pixels"); }

    file.peek(); if (!file.eof()) { throw std::runtime_error("There's some unwanted info after reading."); }
    file.close();


    return std::make_tuple(height, width, std::move(image)); 
}