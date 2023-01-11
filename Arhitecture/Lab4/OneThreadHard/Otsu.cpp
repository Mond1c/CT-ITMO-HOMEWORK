//
// Created by mond1c on 1/4/23.
//

#include "Otsu.h"
#include "FileWriter.h"
#include "Utility.h"
#include <omp.h>
#include <queue>


std::vector<int> Otsu::CalculateThreshold() {
    int f0_ans = 0, f1_ans = 0, f2_ans = 0;
    double v_max = 0;
    int f1, f2;
    double q0, q1, q2, q3;
    double u0, u1, u2, u3;
    double u, v;
    double *q_;
    double *u_;
#pragma omp parallel for num_threads(thread_count) default(none) shared(chunk_size, v_max, f0_ans, f1_ans, f2_ans, f1, f2, Q, U) \
    private(q0, q1, q2, q3, u0, u1, u2, u3, u, v, q_, u_) schedule(static, chunk_size)
        for (int f0 = 0; f0 < 254; ++f0) {
            q_ = Q.data();
            u_ = U.data();
            for (f1 = f0 + 1; f1 < 255; ++f1) {
                for (f2 = f1 + 1; f2 < 256; ++f2) {
                    q0 = q_[f0] - q_[0];
                    q1 = q_[f1] - q_[f0];
                    q2 = q_[f2] - q_[f1];
                    q3 = q_[255] - q_[f2];
                    //double q0 = CalculateChanceForThreshold(0, f0);
                    //double q1 = CalculateChanceForThreshold(f0 + 1, f1);
                    //double q2 = CalculateChanceForThreshold(f1 + 1, f2);
                    //double q3 = CalculateChanceForThreshold(f2 + 1, 255);
                    u0 = (u_[f0] - u_[0]) / q0;
                    u1 = (u_[f1] - u_[f0]) / q1;
                    u2 = (u_[f2] - u_[f1]) / q2;
                    u3 = (u_[255] - u_[f2]) / q3;
                    //double u0 = CalculateAverage(0, f0, q0);
                    //double u1 = CalculateAverage(f0 + 1, f1, q1);
                    //double u2 = CalculateAverage(f1 + 1, f2, q2);
                    //double u3 = CalculateAverage(f2 + 1, 255, q3);
                    u = q0 * u0 + q1 * u1 + q2 * u2 + q3 * u3;
                    v = q0 * (u0 - u) * (u0 - u) + q1 * (u1 - u) * (u1 - u)
                            + q2 * (u2 - u) * (u2 - u) + q3 * (u3 - u) * (u3 - u);
                    if (v > v_max) {
#pragma omp critical
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
    return {f0_ans, f1_ans, f2_ans};
}

void Otsu::Generate() {
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
    return Q[end] - Q[std::max(0, start - 1)];
}

double Otsu::CalculateAverage(int start, int end, double q) {
    return (U[end] - U[std::max(0, start - 1)]) / q;
}

void Otsu::PrecalculateChanceForThreshold() {
    Q[0] = chances_[0];
    for (int i = 1; i < 256; i++) {
        Q[i] = Q[i - 1] + chances_[i];
    }
}

void Otsu::PrecalculateAverage() {
    U[0] = 0;
    for (int i = 1; i < 256; i++) {
        U[i] = U[i - 1] + (i * chances_[i]);
    }
}



