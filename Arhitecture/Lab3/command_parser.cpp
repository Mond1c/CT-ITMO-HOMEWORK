//
// Created by mike on 12/23/22.
//
#include "command_parser.h"
#include <iostream>
#include <sstream>
#include <numeric>

std::vector<std::shared_ptr<utility::symtable_element>> parser::RiscVParser::elements;
std::unordered_map<int, int> parser::RiscVParser::symTable;
int parser::RiscVParser::textAddr;

std::string parser::CommandParser::GetRegister(const std::string &str) {
    int value = std::stoi(str, nullptr, 2);
    switch (value) {
        case 0: return "zero";
        case 1: return "ra";
        case 2: return "sp";
        case 3: return "gp";
        case 4: return "tp";
        case 5: case 6: case 7: return "t" + std::to_string(value - 5);
        case 8: return "s0";
        case 9: return "s1";
        case 10: case 11: case 12: case 13: case 14: case 15:
        case 16: case 17: return "a" + std::to_string(value - 10);
        case 18: case 19: case 20: case 21: case 22: case 23:
        case 24: case 25: case 26: case 27: return "s" + std::to_string(value - 16);
        case 28: case 29: case 30: case 31: return "t" + std::to_string(value - 25);
    }
    throw std::invalid_argument("Unsupported register " + str);
}

std::vector<std::string> parser::CommandParser::GetRiscvCommand(const std::string &str) { // TODO: Add support of symtable
    if (str == "00000000000000000000000001110011") {
        return {"ecall"};
    } else if (str == "00000000000100000000000001110011") {
        return {"ebreak"};
    }
    std::string rs2 = str.substr(7, 5);
    std::string rs1 = str.substr(12, 5);
    std::string rd = str.substr(20, 5);
    std::string opcode = str.substr(25, 7);
    std::string funct7 = str.substr(0, 7);
    std::string funct3 = str.substr(17, 3);
    if (opcode == "0110011") {
        std::string instruction = RiscVParser::ParseArithmeticOP(funct7, funct3);
        return {instruction, GetRegister(rd), GetRegister(rs1), GetRegister(rs2)};
    } else if (opcode == "1110011") {
        std::string instruction = RiscVParser::ParseCSROP(funct3);
        return {instruction, GetRegister(rd), GetRegister(str.substr(0, 12)), GetRegister(rs1)};
    } else if (opcode == "0000011") {
        unsigned int l = std::stoul(std::string(20, str[0]) + str.substr(0, 12), nullptr, 2);
        std::string instruction = RiscVParser::ParseLoadOP(funct3);
        char* buffer = new char[1024];
        sprintf(buffer, "%s\t\t %s, %s(%s)", instruction.c_str(), GetRegister(rd).c_str(),
                std::to_string(l).c_str(), GetRegister(rs1).c_str());
        return {buffer};
    } else if (opcode == "0100011") {
        unsigned int l = std::stoul(std::string(20, str[0]) + str.substr(0, 7) + str.substr(20, 5), nullptr, 2);
        std::string instruction = RiscVParser::ParseStoreOP(funct3);
        char* buffer = new char[1024];
        sprintf(buffer, "%s\t\t %s, %s(%s)", instruction.c_str(), GetRegister(rs2).c_str(),
                std::to_string(l).c_str(), GetRegister(rs1).c_str());
        return {buffer};
    } else if (opcode == "1100011") {
        unsigned int l = std::stoul(str.substr(0, 20) + std::string(12, '0'), nullptr, 2);
        std::string instruction = RiscVParser::ParseLogicalOP(funct3);
        return {instruction, GetRegister(rs1), GetRegister(rs2), RiscVParser::GetLabel(l + 4)};
    } else if (opcode == "0110111" || opcode == "0010111") {
        unsigned int l = std::stoul(str.substr(0, 20) + std::string(12, '0'), nullptr, 2);
        std::string instruction = RiscVParser::ParseBitOP(opcode);
        return {instruction, GetRegister(rd), std::to_string(l)};
    } else if (opcode == "0010011") {
        std::istringstream iss(std::string(20, str[0]) + str.substr(0, 12));
        unsigned int i;
        iss >> i;
        int l = (int)i;
        if (i > std::numeric_limits<unsigned int>::max()) {
            l = std::numeric_limits<int>::min() + (i - std::numeric_limits<unsigned int>::max()) ;
        }
       // std::cout << l << std::endl;
        rs2 = std::to_string(((funct3 == "001" || funct3 == "101")
                ? std::stoul(str.substr(7, 5), nullptr, 2) : l));
        std::string instruction = RiscVParser::ParseArithmeticIOP(funct7, funct3);
        return {instruction, GetRegister(rd), GetRegister(rs1), rs2};
    } else if (opcode == "1101111") {
        std::string instruction = "jal";
        unsigned int l = std::stoul(std::string(12, str[0]) + str.substr(12, 8) + str[11] + str.substr(1, 10) + "0", nullptr, 2);
        return {instruction, GetRegister(rd), GetRegister(rs1), std::to_string(l)};
    } else if (opcode == "1100111") {
        std::string instruction = "jalr";
        unsigned int l = std::stoul(std::string(20, str[0]) + str.substr(0, 12), nullptr, 2);
        return {instruction, GetRegister(rd), GetRegister(rs1), std::to_string(l)};
    } else if (opcode == "0001111") {
        std::string instruction = &"fence" [ *(funct3 == "001" ? ".i" : "")];
        return {instruction};
    }
    throw std::invalid_argument("Invalid command  = " + str);
}

std::string parser::RiscVParser::ParseArithmeticOP(const std::string &funct7, const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    if (funct7 == "0000000") {
        switch (code) {
            case 0: return "add";
            case 1: return "sll";
            case 2: return "slt";
            case 3: return "sltu";
            case 4: return "xor";
            case 5: return "srl";
            case 6: return "or";
            case 7: return "and";
        }
    } else if (funct7 == "0100000") {
        switch (code) {
            case 0: return "sub";
            case 6: return "sra";
        }
    } else if (funct7 == "0000001") {
        switch (code) {
            case 0: return "mul";
            case 1: return "mulh";
            case 2: return "mulhsu";
            case 3: return "mulhu";
            case 4: return "div";
            case 5: return "divu";
            case 6: return "rem";
            case 7: return "remu";
        }
    }
    throw std::invalid_argument("Unsupported Risc-V arithmetic instruction funct7 = " + funct7 + ", funct3 = " + funct3);
}

std::string parser::RiscVParser::ParseCSROP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 1: return "csrrw";
        case 2: return "csrrs";
        case 3: return "csrrc";
        case 5: return "csrwi";
        case 6: return "csrsi";
        case 7: return "csrci";
    }
    throw std::invalid_argument("Unsupported Risc-V csr instruction funct3 = " + funct3);
}

std::string parser::RiscVParser::ParseLoadOP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0: return "lb";
        case 1: return "lh";
        case 2: return "lw";
        case 4: return "lbu";
        case 5: return "lhu";
    }
    throw std::invalid_argument("Unsupported Risc-V load operation funct3 = " + funct3);
}

std::string parser::RiscVParser::ParseStoreOP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0: return "sb";
        case 1: return "sh";
        case 2: return "sw";
    }
    throw std::invalid_argument("Unsupported Risc-V store instruction funct3 = " + funct3);
}

std::string parser::RiscVParser::ParseLogicalOP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0: return "beq";
        case 1: return "bne";
        case 4: return "blt";
        case 5: return "bge";
        case 6: return "bltu";
        case 7: return "bgeu";
    }
    throw std::invalid_argument("Unsupported Risc-V logical instruction funct3 = " + funct3);
}

std::string parser::RiscVParser::ParseBitOP(const std::string &opcode) {
    if (opcode == "0110111") {
        return "lui";
    } else if (opcode == "0010111") {
        return "auipc";
    }
    throw std::invalid_argument("Unsupported Risc-V instruction with opcode = " + opcode);
}

std::string parser::RiscVParser::ParseArithmeticIOP(const std::string &funct7, const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0: return "addi";
        case 1: {
            if (funct7 == "0000000") {
                return "slli";
            }
        }
        case 2: return "slti";
        case 3: return "sltiu";
        case 4: return "xori";
        case 5: {
            if (funct7 == "0000000") {
                return "srli";
            } else if (funct7 == "0100000") {
                return "srai";
            }
        }
        case 6: return "ori";
        case 7: return "andi";
    }
    throw std::invalid_argument("Unsupported Risc-V arithmeticI instruction funct7 = " + funct7 + ", funct3 = " + funct3);
}


std::string parser::RiscVParser::GetLabel(unsigned int x) {
    int addrCommand = textAddr + x;
    if (symTable.count(addrCommand)) {
        return elements[symTable[addrCommand]]->name;
    }
    symTable[addrCommand] = symTable.size();
    elements.push_back(std::make_shared<utility::symtable_element>("LOC_" + std::to_string(addrCommand)));
    return elements[symTable[addrCommand]]->name;
}
