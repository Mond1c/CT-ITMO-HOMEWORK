#include <iostream>
#include "Image.h"
#include "Histogram.h"
#include "Otsu.h"
const int COLOR_COUNT = 256;

int main() {
    Otsu otsu("../rays2.pnm");
    otsu.Calculate();
    return 0;
}
