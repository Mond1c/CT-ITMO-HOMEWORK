#include "utility/FileReader.h"
#include "parser/ElfParser.h"
#include <iostream>

int main() {
    auto parser = parser::ElfParser("../tests/test_elf.elf", "../output.txt");
    parser.Parse();
    return 0;
}