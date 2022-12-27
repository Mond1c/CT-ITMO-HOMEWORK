//
// Created by mike on 12/23/22.
//

#include <sstream>
#include <algorithm>
#include <string>
#include "command_parser.h"
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

    int ElfParser::cnt(int offset, int num) const {
        int ans = 0;
        for (int i = num - 1; i >= 0; --i) {
            ans = ans * 256 + bytes[offset + i];
        }
        return ans;
    }

    void ElfParser::parseHeader() {
        int shoff = cnt(32, 4);
        int shentsiz = cnt(46, 2);
        int shnum = cnt(48, 2);
        int shstrndx = cnt(50, 2);
        int go = shoff + shentsiz * shstrndx;
        int offset12 = cnt(go + 16, 4);
        for (int j = 0, i = shoff; j < shnum; ++j, i += 40) {
            std::string name = GetName(offset12 + cnt(i, 4));
            int params[10];
            for (int k = 0; k < 10; ++k) {
                params[k] = cnt(i + 4 * k, 4);
            }
            sections[name] = std::make_shared<utility::section>(params[0], params[1],
                                                                params[2], params[3],
                                                                params[4], params[5],
                                                                params[6], params[7],
                                                                params[8], params[9]);
        }
    }

    std::string ElfParser::GetName(int offset) const {
        std::stringstream ss;
        while (bytes[offset] != 0) {
            ss << (char) bytes[offset++];
        }
        return ss.str();
    }

    void ElfParser::parseText() {
        std::shared_ptr<utility::section> text = sections[".text"];
        writer->writeLine(".text");
        int offset = text->offset;
        int size = text->size;
        int addr = text->addr;
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
            std::string command = CommandParser::GetRiscvCommand(addr + i, ss.str());
            std::string label;
            if (symTable.count(addr + i) && elements[symTable[addr + i]]->type == "FUNC") {
                label = elements[symTable[addr + i]]->name;
            }
            char* buffer = new char[1024];
            if (!label.empty()) {
                sprintf(buffer, "%08x   <%s>:", addr + i, label.c_str());
                writer->writeLine(buffer);
            }
            delete[] buffer;
            writer->write(command);
            offset += 4;
        }
        writer->writeLine("");
    }

    void ElfParser::parseSymTab() {
        auto symtab = sections[".symtab"];
        int offset = symtab->offset;
        int num = symtab->size / 16;
        for (int i = 0; i < num; ++i) {
            std::stringstream ss;
            for (int j = 0; j < 4; ++j) {
                std::string str = decimal_to_binary(bytes[offset + i * 16 + 12 + j]);
                ss << str;
            }
            std::string str = ss.str();
            std::string name = GetName(cnt(offset + i * 16, 4) + sections[".strtab"]->offset);
            int value = cnt(offset + i * 16 + 4, 4);
            int size = cnt(offset + i * 16 + 8, 4);
            std::string infoStr = str.substr(0, 8);
            std::reverse(infoStr.begin(), infoStr.end());
            std::string otherStr = str.substr(8, str.size() - 8);
            std::reverse(otherStr.begin(), otherStr.end());
            int info = std::stoi(infoStr, nullptr, 2);
            int other = std::stoi(otherStr, nullptr, 2);
            std::cout << value << " " << size << " " << info << " " << other << std::endl;
            symTable[value] = elements.size();
            elements.push_back(std::make_shared<utility::symtable_element>(name, value, size, i, info, other));


        }
        RiscVParser::elements = elements;
        RiscVParser::symTable = symTable;
        RiscVParser::textAddr = sections[".text"]->addr;
    }

    void ElfParser::dumpSymTable() {
        writer->writeLine(".symtab");
        writer->writeLine("Symbol Value          \tSize Type \tBind \tVis   \tIndex Name");
        for (const auto &element: elements) {
            writer->write(element->GetString());
        }
    }

    void ElfParser::parse() {
        parseHeader();
        parseSymTab();
        parseText();
        dumpSymTable();
    }
} // parser