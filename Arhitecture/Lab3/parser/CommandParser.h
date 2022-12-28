#ifndef ASMPARSER_COMMAND_PARSER_H
#define ASMPARSER_COMMAND_PARSER_H

#include <string>
#include <memory>
#include <unordered_map>
#include <vector>
#include <sstream>
#include "../utility/SymtableElement.h"
#include "ElfParser.h"

namespace parser {
    class CommandParser {
    private:
        int labelCount = 0;
        std::vector<std::shared_ptr<utility::SymtableElement>> &elements;
        std::unordered_map<int, int> &symTable;
        int textAddr;
        std::vector<uint8_t> &bytes;

        [[nodiscard]] std::string GetName(int offset) const {
            std::stringstream ss;
            while (bytes[offset] != 0) {
                ss << (char) bytes[offset++];
            }
            return ss.str();
        }

    private:
        static std::string formatFor3Args;
        static std::string formatFor2Args;
        static std::string formatForLSJ;
    public:
        CommandParser(std::vector<uint8_t> &bytes, std::vector<std::shared_ptr<utility::SymtableElement>> &elements,
                      std::unordered_map<int, int> &symTable, int textAddr)
                : bytes(bytes), elements(elements), symTable(symTable), textAddr(textAddr) {}

    public:
        std::string GetLabel(int addrCommand, bool isJ);

        std::string GetRiscvCommand(int addr, const std::string &str);

        static std::string GetRegister(const std::string &str);


    };

    class RiscVParser {
    public:
        static std::string ParseArithmeticOP(const std::string &funct7, const std::string &funct3);

        static std::string ParseCSROP(const std::string &funct3);

        static std::string ParseLoadOP(const std::string &funct3);

        static std::string ParseStoreOP(const std::string &funct3);

        static std::string ParseLogicalOP(const std::string &funct3);

        static std::string ParseBitOP(const std::string &opcode);

        static std::string ParseArithmeticIOP(const std::string &funct7, const std::string &funct3);

    };
}

#endif
