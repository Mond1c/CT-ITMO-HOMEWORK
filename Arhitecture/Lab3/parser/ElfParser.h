//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_ELFPARSER_H
#define ASMPARSER_ELFPARSER_H

#include "../utility/FileWriter.h"
#include "../utility/FileReader.h"
#include "../utility/SymtableElement.h"
#include "../utility/Section.h"
#include "CommandParser.h"
#include "SymtableParser.h"

#include <unordered_map>
#include <vector>

namespace parser {

    class ElfParser {
    private:
        std::vector<uint8_t> bytes;
        std::unique_ptr<utility::FileReader> reader;
        std::unique_ptr<utility::FileWriter> writer;
        std::vector<std::shared_ptr<utility::SymtableElement>> elements;
        std::unordered_map<std::string, std::shared_ptr<utility::Section>> sections;
        std::unordered_map<int, int> symTable;
    public:
        ElfParser(const std::string &inputFile, const std::string &outputFile) {
            reader = std::make_unique<utility::FileReader>(inputFile);
            writer = std::make_unique<utility::FileWriter>(outputFile);
            bytes = reader->ReadBytes();
        }

        ~ElfParser() = default;

    public:
        void Parse();

    private:
        void ParseHeader();

        void ParseText();

        void ParseSymtab();

        void SaveSymtab();

        [[nodiscard]] int GetValue(int offset, int size) const;

        [[nodiscard]] std::string GetName(int offset) const;
    };

} // parser

#endif //ASMPARSER_ELFPARSER_H
