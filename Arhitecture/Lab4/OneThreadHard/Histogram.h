//
// Created by mond1c on 1/4/23.
//

#ifndef ONETHREADHARD_HISTOGRAM_H
#define ONETHREADHARD_HISTOGRAM_H

#include <vector>
#include <map>
#include "Image.h"

class Histogram {
public:
    static std::map<int, int> Generate(std::shared_ptr<utility::Image> &image);
    static void Print(std::map<int, int> &histogram);
};


#endif //ONETHREADHARD_HISTOGRAM_H
