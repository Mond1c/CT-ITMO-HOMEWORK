//
// Created by mond1c on 1/4/23.
//

#ifndef ONETHREADHARD_OTSU_H
#define ONETHREADHARD_OTSU_H
#include "Image.h"
#include "Histogram.h"

class Otsu {
private:
    std::shared_ptr<utility::Image> image_;
    std::map<int, int> histogram_;
    std::map<int, double> chances_;
public:
    explicit Otsu(const std::string& fileName)
        : image_(std::make_shared<utility::Image>(fileName))
        , histogram_(Histogram::Generate(image_)) {
        CalculateChances();
    }
public:
    void Generate();
private:
    void CalculateChances();
    double CalculateChanceForThreshold(int start, int end);
    double CalculateAverage(int start, int end, double q);
    std::vector<int> CalculateThreshold();
};


#endif //ONETHREADHARD_OTSU_H
