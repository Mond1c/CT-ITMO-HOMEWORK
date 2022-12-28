//
// Created by mike on 12/23/22.
//

#include <sstream>
#include <algorithm>
#include <string>
#include "CommandParser.h"
#include "ElfParser.h"


namespace parser {
    namespace {
        std::string decimal_to_binary(int x) {
            std::stringstream ss;
            for (int i = 0; i < 8; ++i) {
                ss << (x % 2);
                x /= 2;
            }
            return ss.str();
        }
    }

    int ElfParser::GetValue(int offset, int size) const {
        int ans = 0;
        for (int i = size - 1; i >= 0; --i) {
            std::cout << (int) bytes[offset + i] << " ";
            ans = ans * 256 + bytes[offset + i];
        }
        std::cout << ans << std::endl;
        return ans;
    }

    void ElfParser::ParseHeader() {
        int e_shoff = GetValue(0x20, 4);
        int e_shentsiz = GetValue(0x2E, 2);
        int e_shnum = GetValue(0x30, 2);
        int e_shstrndx = GetValue(0x32, 2);
        int off = e_shoff + e_shentsiz * e_shstrndx;
        int offset = GetValue(off + 16, 4);
        for (int j = 0, i = e_shoff; j < e_shnum; ++j, i += 40) {
            std::string name = GetName(offset + GetValue(i, 4));
            int params[10];
            for (int k = 0; k < 10; ++k) {
                params[k] = GetValue(i + 4 * k, 4);
            }
            sections[name] = std::make_shared<utility::Section>(params[0], params[1],
                                                                params[2], params[3],
                                                                params[4], params[5],
                                                                params[6], params[7],
                                                                params[8], params[9]);
        }
    }

    std::string ElfParser::GetName(int offset) const {
        std::stringstream ss;
        while (bytes[offset] > 0) {
            ss << (char) bytes[offset++];
        }
        return ss.str();
    }

    void ElfParser::ParseText() {
        std::shared_ptr<utility::Section> text = sections[".text"];
        writer->writeLine(".text");
        int offset = text->offset;
        int size = text->size;
        int addr = text->addr;
        std::cout << addr << std::endl;
        CommandParser parser(bytes, elements, symTable, addr);
        for (int i = 0; i < size; i += 4) {
            std::stringstream ss;
            std::string byte1 = decimal_to_binary(bytes[offset]);
            std::reverse(byte1.begin(), byte1.end());
            std::string byte2 = decimal_to_binary(bytes[offset + 1]);
            std::reverse(byte2.begin(), byte2.end());
            std::string byte3 = decimal_to_binary(bytes[offset + 2]);
            std::reverse(byte3.begin(), byte3.end());
            std::string byte4 = decimal_to_binary(bytes[offset + 3]);
            std::reverse(byte4.begin(), byte4.end());
            ss << byte4 << byte3 << byte2 << byte1;
            std::string command = parser.GetRiscvCommand(addr + i, ss.str());
            std::string label;
            if (symTable.count(addr + i) && elements[symTable[addr + i]]->type == "FUNC") {
                label = elements[symTable[addr + i]]->name;
            }
            if (!label.empty()) {
                writer->writeLine(utility::GetFormatString("%08x   <%s>:", addr + i, label.c_str()));
            }
            writer->write(command);
            offset += 4;
        }
        writer->writeLine("");
    }

    void ElfParser::ParseSymtab() {
        auto symtab = sections[".symtab"];
        int offset = symtab->offset;
        int num = symtab->size / 16;
        int strOffset = sections[".strtab"]->offset;
        for (int i = 0; i < num; ++i) {
            std::stringstream ss;
            for (int j = 0; j < 4; ++j) {
                std::string str = decimal_to_binary(bytes[offset + i * 16 + 12 + j]);
                ss << str;
            }
            std::string str = ss.str();
            std::string name = GetName(GetValue(offset + i * 16, 4) + strOffset);
            std::cout << name << std::endl;
            std::string infoStr = str.substr(0, 8);
            std::reverse(infoStr.begin(), infoStr.end());
            std::string otherStr = str.substr(8, str.size() - 8);
            std::reverse(otherStr.begin(), otherStr.end());
            int8_t value = GetValue(offset + i * 16 + 4, 4);
            int8_t size = GetValue(offset + i * 16 + 8, 4);
            int8_t info = std::stoi(infoStr, nullptr, 2);
            int8_t other = std::stoi(otherStr, nullptr, 2);
            symTable[value] = elements.size();
            elements.push_back(std::make_shared<utility::SymtableElement>(name, value, size, i, info, other));
        }
    }

    void ElfParser::SaveSymtab() {
        writer->writeLine(".symtab");
        writer->writeLine("Symbol Value          \t  Size Type \tBind \t Vis   \t   Index Name");
        for (const auto &element: elements) {
            writer->write(element->GetString());
        }
    }

    void ElfParser::Parse() {
        ParseHeader();
        ParseSymtab();
        ParseText();
        SaveSymtab();
    }
} // parser