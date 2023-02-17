#include <iostream>
#include <stdint.h>
#include <cmath>

typedef int16_t fixed_point_t;
#define FIXED_POINT_FRACTIONAL_BITS 6

double fixed_to_double(fixed_point_t input) {
    return ((double) input / (double)(1 << FIXED_POINT_FRACTIONAL_BITS));
}

fixed_point_t double_to_fixed(double input) {
    return (fixed_point_t)(std::round(0.5 * (input * (1 << FIXED_POINT_FRACTIONAL_BITS))) * 2.0);
}


fixed_point_t mult_16_16(fixed_point_t a, fixed_point_t b) {
    return (fixed_point_t)(((int)a * b) / (64));
}

int main() {
    std::cout << std::round(1.25) << std::endl;
    fixed_point_t a = double_to_fixed(233.078);
    fixed_point_t b = double_to_fixed(3.672);
    fixed_point_t c = mult_16_16(a, b);
    printf("a = %x\nb = %x\na * b = %x\n", a, b, mult_16_16(a, b));
    printf("%f", fixed_to_double(c));
    return 0;
}
