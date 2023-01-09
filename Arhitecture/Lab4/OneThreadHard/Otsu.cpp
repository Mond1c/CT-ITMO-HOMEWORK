//
// Created by mond1c on 1/4/23.
//

#include "Otsu.h"
#include "FileWriter.h"
#include "Utility.h"
#include <omp.h>


std::vector<int> Otsu::CalculateThreshold() {
    int f0_ans = 0, f1_ans = 0, f2_ans = 0;
    double v_max = 0;
#pragma omp parallel for schedule(dynamic)
    for (int f0 = 0; f0 < 254; ++f0) {
        for (int f1 = f0 + 1; f1 < 255; ++f1) {
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
                double v0 = q0 * (u0 - u) * (u0 - u);
                double v1 = q1 * (u1 - u) * (u1 - u);
                double v2 = q2 * (u2 - u) * (u2 - u);
                double v3 = q3 * (u3 - u) * (u3 - u);
                double v = v0 + v1 + v2 + v3;
#pragma omp critical
                {
                    if (v > v_max) {
                        f0_ans = f0;
                        f1_ans = f1;
                        f2_ans = f2;
                        v_max = v;
                    }
                }
            }
        }
    }
  //  std::cout << v_max << std::endl;
    return {f0_ans, f1_ans, f2_ans};
}

void Otsu::Generate() {
   // Histogram::Print(histogram_);
    auto f = CalculateThreshold();
    int f0 = f[0];
    int f1 = f[1];
    int f2 = f[2];
    std::cout << utility::GetFormatString("%u %u %u\n", f0, f1, f2);
    utility::FileWriter writer(outputFileName_);
    writer.WriteLine("P5");
    writer.WriteLine(std::to_string(image_->GetWidth()) + " " + std::to_string(image_->GetHeight()));
    writer.WriteLine("255");
    for (int i = 0; i < image_->GetHeight(); ++i) {
        for (int j = 0; j < image_->GetWidth(); ++j) {
            char ch;
            if (image_->GetPixel(i, j) <= f0) {
                ch = (char)0;
            } else if (image_->GetPixel(i, j) <= f1) {
                ch = (char)84;
            } else if (image_->GetPixel(i, j) <= f2) {
                ch = (char)170;
            } else {
                ch = (char)255;
            }
            writer.Write(ch);
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
    return q_[end] - q_[std::max(0, start - 1)];
}

double Otsu::CalculateAverage(int start, int end, double q) {
    return (u_[end] - u_[std::max(0, start - 1)]) / q;
}

void Otsu::PrecalculateChanceForThreshold() {
    q_[0] = chances_[0];
    for (int i = 1; i < 256; i++) {
        q_[i] = q_[i - 1] + chances_[i];
    }
}

void Otsu::PrecalculateAverage() {
    u_[0] = 0;
    for (int i = 1; i < 256; i++) {
        u_[i] = u_[i - 1] + (i * chances_[i]);
    }
}



