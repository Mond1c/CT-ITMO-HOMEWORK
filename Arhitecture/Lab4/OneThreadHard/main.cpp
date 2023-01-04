#include <iostream>
#include "Image.h"
#include "Histogram.h"

int main() {
    auto image = std::make_shared<utility::Image>("../rays2.pnm");
    auto histogram = Histogram::Generate(image);
    Histogram::Print(histogram);
    return 0;
}
