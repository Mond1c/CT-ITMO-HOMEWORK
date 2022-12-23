#ifndef COMMAND_PARSER_H
#define COMMAND_PARSER_H
#include <string>
#include <memory>
#include "utility/riscv_command.h"

namespace parser {
    class CommandParser {
    public:
        static std::shared_ptr<utility::riscv_command> GetRiscvCommand(const std::string& str);
        static std::string GetRegister(const std::string& str);
    };

    class RiscVParser {
    public:
        static std::string ParseArithmeticOP(const std::string& funct7, const std::string& funct3);
        static std::string ParseCSROP(const std::string& funct3);
        static std::string ParseLoadOP(const std::string& funct3);
        static std::string ParseStoreOP(const std::string& funct3);
        static std::string ParseLogicalOP(const std::string& funct3);
        static std::string ParseBitOP(const std::string& opcode);
        static std::string ParseArithmeticIOP(const std::string& funct7, const std::string& funct3);
    };
}

#endif