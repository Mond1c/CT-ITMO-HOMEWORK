#ifndef COMMAND_PARSER_H
#define COMMAND_PARSER_H

#include <string>
#include <memory>
#include <unordered_map>
#include <vector>
#include "utility/riscv_command.h"
#include "utility/symtable_element.h"

namespace parser {
    class CommandParser {
    private:
        static std::string formatFor3Args;
        static std::string formatFor2Args;
        static std::string formatForLSJ;
    public:
        static std::string GetRiscvCommand(int addr, const std::string &str);

        static std::string GetRegister(const std::string &str);

        template<typename... Args>
        static std::string GetFormatString(const std::string &format, Args... args);
    };

    class RiscVParser {
    public:
        static std::vector<std::shared_ptr<utility::symtable_element>> elements;
        static std::unordered_map<int, int> symTable;
        static int textAddr;
    public:
        static std::string GetLabel(unsigned int x);

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