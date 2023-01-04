//
// Created by mond1c on 1/4/23.
//

#include "Otsu.h"
#include "FileWriter.h"
#include <omp.h>


std::vector<int> Otsu::CalculateThreshold() {
    /*
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
     */
    int f0_ans = 0, f1_ans = 0, f2_ans = 0;
    double v_max = 0;
    for (int f0 = 0; f0 < 254; ++f0) {
        std::cout << f0 << std::endl;
        for (int f1 = f0 + 1; f1 < 255; ++f1) {
                #pragma omp parallel for
                for (int f2 = f1 + 1; f2 < 256; ++f2) {
                    double q0 = CalculateChanceForThreshold(0, f0);
                    double q1 = CalculateChanceForThreshold(f0 + 1, f1);
                    double q2 = CalculateChanceForThreshold(f1 + 1, f2);
                    double q3 = CalculateChanceForThreshold(f2 + 1, 255);
                    double u0 = CalculateAverage(0, f0, q0);
                    double u1 = CalculateAverage(f0 + 1, f1, q1);
                    double u2 = CalculateAverage(f1 + 1, f2, q2);
                    double u3 = CalculateAverage(f2 + 1, 255, q3);
                    double u = q0 * u0 + q1 * u1 + q2 * u2 + q3 * u3;
                    double v = q0 * (u0 - u) * (u0 - u) + q1 * (u1 - u) * (u1 - u)
                               + q2 * (u2 - u) * (u2 - u) + q3 * (u3 - u) * (u3 - u);
                    if (v > v_max) {
                        f0_ans = f0;
                        f1_ans = f1;
                        f2_ans = f2;
                        v_max = v;
                    }
                }
        }
    }
    return {f0_ans, f1_ans, f2_ans};
}

void Otsu::Generate() {
    auto f = CalculateThreshold();
    int f0 = f[0];
    int f1 = f[1];
    int f2 = f[2];
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
    }
}

void Otsu::CalculateChances() {
    int pixelCount = image_->GetHeight() * image_->GetWidth();
    for (const auto& item : histogram_) {
        chances_[item.first] = item.second / (double) pixelCount;
    }
}

double Otsu::CalculateChanceForThreshold(int start, int end) {
    double sum = 0;
    for (int i = start; i <= end; ++i) {
        sum += chances_[i];
    }
    return sum;
}

double Otsu::CalculateAverage(int start, int end, double q) {
    double u = 0;
    for (int i = start; i <= end; ++i) {
        u += (i * chances_[i]) / q;
    }
    return u;
}



