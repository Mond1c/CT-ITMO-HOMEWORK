//
// Created by mond1c on 1/4/23.
//

#ifndef ONETHREADHARD_OTSU_H
#define ONETHREADHARD_OTSU_H
#include <utility>

#include "Image.h"
#include "Histogram.h"

static int chunk_size = 1;

class Otsu {
private:
    std::shared_ptr<utility::Image> image_;
    std::map<int, int> histogram_;
    std::map<int, double> chances_;
    std::string outputFileName_;
    bool isOpenMPEnabled_;
public:
    explicit Otsu(const std::string& inputFileName, std::string  outputFileName, bool isOpenMPEnabled)
        : image_(std::make_shared<utility::Image>(inputFileName))
        , histogram_(Histogram::Generate(image_))
        , outputFileName_(std::move(outputFileName))
        , isOpenMPEnabled_(isOpenMPEnabled)
        {
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
