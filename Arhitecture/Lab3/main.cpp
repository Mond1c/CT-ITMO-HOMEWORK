#include "utility/FileReader.h"
#include "parser/ElfParser.h"
#include <iostream>

int main(int argc, char** argv) {
    if (argc != 3) {
        std::cerr << "You need two write input file and output file in args" << std::endl;
    }
    try {
        auto parser = parser::ElfParser(argv[1], argv[2]);
        parser.Parse();
    } catch (std::exception& e) {
        std::cerr << e.what() << std::endl;
    }
    return 0;
}