//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_ELFPARSER_H
#define ASMPARSER_ELFPARSER_H

#include "utility/file_writer.hpp"
#include "utility/file_reader.hpp"
#include "utility/symtable_element.h"
#include "utility/Section.h"
#include "command_parser.h"
#include "symtable_parser.h"

#include <unordered_map>
#include <vector>

namespace parser {

    class ElfParser {
    private:
        std::vector<uint8_t> bytes;
        std::unique_ptr<utility::FileReader> reader;
        std::unique_ptr<utility::FileWriter> writer;
        std::vector<std::shared_ptr<utility::symtable_element>> elements;
        std::unordered_map<std::string, std::shared_ptr<utility::section>> sections;
        std::unordered_map<int, int> symTable;
    public:
        ElfParser(const std::string &inputFile, const std::string &outputFile) {
            reader = std::make_unique<utility::FileReader>(inputFile);
            writer = std::make_unique<utility::FileWriter>(outputFile);
            bytes = reader->ReadBytes();
        }

        ~ElfParser() = default;

    public:
        void parse();

    public:
        void parseHeader();

        void parseText();

        void parseSymTab();

        void dumpSymTable();

        int cnt(int offset, int num) const;

        std::string GetName(int offset) const;
    };

} // parser

#endif //ASMPARSER_ELFPARSER_H
