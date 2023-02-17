#include "half.hpp"
#include <iostream>
#include <iomanip>

int main() {
    using half_float::half;
    half ch(69.49);
    half dh(3.78);
    float cs = 69.49f;
    float ds = 3.78f;
    half x = ch + dh;
    float y = cs + ds;
    float z = ch + ds;
    float w = cs + dh;
    //printf("%f %f %f %f", x, y, z, w);
    std::cout << std::setprecision(10) << "x = " << x 
        << " y =  " << y << " z = " << z << " w = " << w << std::endl;
}
