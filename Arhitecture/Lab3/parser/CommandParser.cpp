//
// Created by mike on 12/23/22.
//
#include "CommandParser.h"
#include "../utility/Utility.h"
#include <iostream>
#include <sstream>
#include <numeric>

std::string parser::CommandParser::formatFor3Args = "   %05x:\t%08x\t%7s\t%s, %s, %s\n";
std::string parser::CommandParser::formatFor2Args = "   %05x:\t%08x\t%7s\t%s, %s\n";
std::string parser::CommandParser::formatForLSJ = "   %05x:\t%08x\t%7s\t%s, %s(%s)\n";

std::string parser::CommandParser::GetRegister(const std::string &str) {
    int value = std::stoi(str, nullptr, 2);
    switch (value) {
        case 0:
            return "zero";
        case 1:
            return "ra";
        case 2:
            return "sp";
        case 3:
            return "gp";
        case 4:
            return "tp";
        case 5:
        case 6:
        case 7:
            return "t" + std::to_string(value - 5);
        case 8:
            return "s0";
        case 9:
            return "s1";
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
            return "a" + std::to_string(value - 10);
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
        case 27:
            return "s" + std::to_string(value - 16);
        case 28:
        case 29:
        case 30:
        case 31:
            return "t" + std::to_string(value - 25);
    }
    throw std::invalid_argument("Unsupported register " + str);
}

std::string parser::CommandParser::GetRiscvCommand(int addr, const std::string &str) { // TODO: Add support of symtable
    unsigned int code = std::stoul(str, nullptr, 2);
    if (str == "00000000000000000000000001110011") {
        return utility::GetFormatString("   %05x:\t%08x\t%7s\n", addr, code, "ecall");
    } else if (str == "00000000000100000000000001110011") {
        return utility::GetFormatString("   %05x:\t%08x\t%7s\n", addr, code, "ebreak");
    }
    std::string rs2 = str.substr(7, 5);
    std::string rs1 = str.substr(12, 5);
    std::string rd = str.substr(20, 5);
    std::string opcode = str.substr(25, 7);
    std::string funct7 = str.substr(0, 7);
    std::string funct3 = str.substr(17, 3);
    if (opcode == "0110011") {
        std::string instruction = RiscVParser::ParseArithmeticOP(funct7, funct3);
        return utility::GetFormatString(formatFor3Args, addr, code, instruction.c_str(), GetRegister(rd).c_str(),
                               GetRegister(rs1).c_str(), GetRegister(rs2).c_str());
    } else if (opcode == "1110011") {
        std::string instruction = RiscVParser::ParseCSROP(funct3);
        return utility::GetFormatString(formatFor3Args, addr, code, instruction.c_str(), GetRegister(rd).c_str(),
                               GetRegister(str.substr(0, 12)).c_str(), GetRegister(rs1).c_str());
    } else if (opcode == "0000011") {
        int imm = (int) std::stoul(str.substr(0, 12), nullptr, 2);
        std::string instruction = RiscVParser::ParseLoadOP(funct3);
        return utility::GetFormatString(formatForLSJ, addr, code, instruction.c_str(), GetRegister(rd).c_str(),
                               std::to_string(imm).c_str(), GetRegister(rs1).c_str());
    } else if (opcode == "0100011") {
        int imm = (int) std::stoul(str.substr(0, 7) + str.substr(20, 5), nullptr, 2);
        std::string instruction = RiscVParser::ParseStoreOP(funct3);
        return utility::GetFormatString(formatForLSJ, addr, code, instruction.c_str(), GetRegister(rs2).c_str(),
                               std::to_string(imm).c_str(),
                               GetRegister(rs1).c_str());
    } else if (opcode == "1100011") {
        int imm = (int) std::stoul(str.substr(24, 1) + str.substr(20, 4) + str.substr(1, 6) + str[0] , nullptr, 2);
        std::string instruction = RiscVParser::ParseLogicalOP(funct3);
        //std::cout << "imm: " << utility::GetFormatString("%05x", imm) << std::endl;
        if (imm > 4096) {
            imm = imm - 4096;
            imm = -imm;
        } else if (imm < -4096) {
            imm = imm + 4096;
            imm = -imm;
        }
        return utility::GetFormatString(formatFor3Args, addr, code, instruction.c_str(), GetRegister(rs1).c_str(),
                               GetRegister(rs2).c_str(), GetLabel(addr + imm + 4, false).c_str());
    } else if (opcode == "0110111" || opcode == "0010111") {
        int imm = (int) std::stoul(str.substr(0, 20), nullptr, 2);
        std::string instruction = RiscVParser::ParseBitOP(opcode);
        return utility::GetFormatString(formatFor2Args, addr, code, instruction.c_str(), GetRegister(rd).c_str(),
                               utility::GetFormatString("0x%x", imm).c_str());
    } else if (opcode == "0010011") {
        int imm = (int) std::stoul(std::string(20, str[0]) + str.substr(0, 12), nullptr, 2);
        rs2 = std::to_string(((funct3 == "001" || funct3 == "101")
                              ? (int) std::stoul(str.substr(7, 5), nullptr, 2) : imm));
        std::string instruction = RiscVParser::ParseArithmeticIOP(funct7, funct3);
        return utility::GetFormatString(formatFor3Args, addr, code, instruction.c_str(), GetRegister(rd).c_str(),
                               GetRegister(rs1).c_str(), rs2.c_str());
    } else if (opcode == "1101111") {
        std::string instruction = "jal";
        int imm = std::stoi(str.substr(12, 8) + str[11] + str.substr(1, 10) + str[0],
                            nullptr, 2);
        if (imm > 1048505) {
            imm = imm - 1048505;
            imm = -imm;
        } else if (imm < -1048506) {
            imm = imm + 1048505;
            imm = -imm;
        }
        return utility::GetFormatString("   %05x:\t%08x\t%7s\t%s, %05x(%s)\n", addr, code, instruction.c_str(),
                               GetRegister(rd).c_str(),
                               addr + imm, GetLabel(addr + imm, true).c_str());
    } else if (opcode == "1100111") {
        std::string instruction = "jalr";
        int imm = (int) std::stoul(std::string(20, str[0]) + str.substr(0, 12), nullptr, 2);
        return utility::GetFormatString(formatForLSJ, addr, code, instruction.c_str(), GetRegister(rd).c_str(),
                               std::to_string(imm).c_str(), GetRegister(rs1).c_str());
    } else if (opcode == "0001111") {
        std::string part = (funct3 == "001" ? ".i" : "");
        return utility::GetFormatString(formatFor2Args, addr, code, ("fence" + part).c_str(), "iorw", "iorw");
    }
    throw std::invalid_argument("Invalid command  = " + str);
}


std::string parser::RiscVParser::ParseArithmeticOP(const std::string &funct7, const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    if (funct7 == "0000000") {
        switch (code) {
            case 0:
                return "add";
            case 1:
                return "sll";
            case 2:
                return "slt";
            case 3:
                return "sltu";
            case 4:
                return "xor";
            case 5:
                return "srl";
            case 6:
                return "or";
            case 7:
                return "and";
        }
    } else if (funct7 == "0100000") {
        switch (code) {
            case 0:
                return "sub";
            case 6:
                return "sra";
        }
    } else if (funct7 == "0000001") {
        switch (code) {
            case 0:
                return "mul";
            case 1:
                return "mulh";
            case 2:
                return "mulhsu";
            case 3:
                return "mulhu";
            case 4:
                return "div";
            case 5:
                return "divu";
            case 6:
                return "rem";
            case 7:
                return "remu";
        }
    }
    return  "unknown_instruction";
}

std::string parser::RiscVParser::ParseCSROP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 1:
            return "csrrw";
        case 2:
            return "csrrs";
        case 3:
            return "csrrc";
        case 5:
            return "csrwi";
        case 6:
            return "csrsi";
        case 7:
            return "csrci";
    }
    return  "unknown_instruction";
}

std::string parser::RiscVParser::ParseLoadOP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0:
            return "lb";
        case 1:
            return "lh";
        case 2:
            return "lw";
        case 4:
            return "lbu";
        case 5:
            return "lhu";
    }
    return  "unknown_instruction";
}

std::string parser::RiscVParser::ParseStoreOP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0:
            return "sb";
        case 1:
            return "sh";
        case 2:
            return "sw";
    }
    return  "unknown_instruction";
}

std::string parser::RiscVParser::ParseLogicalOP(const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0:
            return "beq";
        case 1:
            return "bne";
        case 4:
            return "blt";
        case 5:
            return "bge";
        case 6:
            return "bltu";
        case 7:
            return "bgeu";
    }
    return  "unknown_instruction";
}

std::string parser::RiscVParser::ParseBitOP(const std::string &opcode) {
    if (opcode == "0110111") {
        return "lui";
    } else if (opcode == "0010111") {
        return "auipc";
    }
    return  "unknown_instruction";
}

std::string parser::RiscVParser::ParseArithmeticIOP(const std::string &funct7, const std::string &funct3) {
    int code = std::stoi(funct3, nullptr, 2);
    switch (code) {
        case 0:
            return "addi";
        case 1: {
            if (funct7 == "0000000") {
                return "slli";
            }
        }
        case 2:
            return "slti";
        case 3:
            return "sltiu";
        case 4:
            return "xori";
        case 5: {
            if (funct7 == "0000000") {
                return "srli";
            } else if (funct7 == "0100000") {
                return "srai";
            }
        }
        case 6:
            return "ori";
        case 7:
            return "andi";
    }
    return  "unknown_instruction";
}


std::string parser::CommandParser::GetLabel(int addrCommand, bool isJ) {
    if (symTable.count(addrCommand)) {
        return elements[symTable[addrCommand]]->name;
    }
    symTable[addrCommand] = elements.size();
    if (isJ) {
        elements.push_back(std::make_shared<utility::SymtableElement>(utility::GetFormatString("0x%x", addrCommand)));
    } else {
        elements.push_back(std::make_shared<utility::SymtableElement>(utility::GetFormatString("L%i", labelCount++)));
    }
    return elements[symTable[addrCommand]]->name;
}


