#include <fstream>

#include "otsuMethod.hpp"

void writeImage(int height, int width, image_type const & image, const char * filename) {
    std::ofstream file(filename, std::ios::binary);

    if (!file.good()) {
        throw std::runtime_error("Couldn't open a file for writing");
    }

    file << "P5\n" << height << ' ' << width << "\n255" << std::endl;
    for (auto const pixel: image) {
        file << pixel;
    }
}
