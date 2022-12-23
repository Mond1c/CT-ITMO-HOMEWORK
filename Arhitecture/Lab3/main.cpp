#include "utility/file_reader.hpp"
#include <iostream>

int main() {
    utility::FileReader reader("../test.elf");
    reader.ReadBytes();
    return 0;
}