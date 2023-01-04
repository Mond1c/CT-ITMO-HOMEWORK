//
// Created by mond1c on 1/4/23.
//

#include "Otsu.h"
#include "FileWriter.h"

int Otsu::CalculateThreshold(int start, int end) {
    int pixelsCount = image_->GetHeight() * image_->GetWidth();
    double sum = 0;
    for (int i = start; i < end; ++i) {
        sum += i * histogram_[i];
    }
    double sumB = 0;
    double wB = 0;
    double wF = 0;
    double varMax = 0;
    int threshold = 0;

    for (int i = start; i < end; ++i) {
        wB += histogram_[i];
        if (wB == 0) continue;
        wF = pixelsCount - wB;
        if (wF == 0) break;
        sumB += (double)(i * histogram_[i]);
        double mB = sumB / wB;
        double mF = (sum - sumB) / wF;
        double varBetween = (double)wB * (double)wF * (mB - mF) * (mB - mF);
        if (varBetween > varMax) {
            varMax = varBetween;
            threshold = i;
        }
    }
    return threshold;
}

void Otsu::Calculate() {
    int f1 = CalculateThreshold(0, 256);
    int f0 = CalculateThreshold(0, f1);
    int f2 = CalculateThreshold(f1 + 1, 256);
    std::cout << f0 << " " << f1 << " " << f2 << std::endl;
    utility::FileWriter writer("../output.pnm");
    writer.WriteLine("P5");
    writer.WriteLine(std::to_string(image_->GetWidth()) + " " + std::to_string(image_->GetHeight()));
    writer.WriteLine("255");
    for (int i = 0; i < image_->GetHeight(); ++i) {
        for (int j = 0; j < image_->GetWidth(); ++j) {
            if (image_->GetPixel(i, j) <= f0) {
                writer.Write((char)0);
            } else if (image_->GetPixel(i, j) <= f1) {
                writer.Write((char)84);
            } else if (image_->GetPixel(i, j) <= f2) {
                writer.Write((char)170);
            } else {
                writer.Write((char)255);
            }
        }
        writer.WriteLine("");
    }
}
