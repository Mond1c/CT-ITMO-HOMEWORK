#include "utility/file_reader.hpp"
#include "ElfParser.h"
#include <iostream>

int main() {
    auto parser = parser::ElfParser("../test_elf.elf", "../output.txt");
    parser.parse();
    return 0;
}