//
// Created by mike on 12/23/22.
//

#include <sstream>
#include "ElfParser.h"

namespace parser {
    namespace {
        std::string decimal_to_binary(int i) {
            std::stringstream ss;
            while (i) {
                ss << i % 2;
                i /= 2;
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
        int go = shoff + shentsiz + shstrndx;
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
        for (int i = 0; i < size; i += 2) {
            std::stringstream ss;
            ss  << decimal_to_binary(bytes[offset + 3])
                << decimal_to_binary(bytes[offset + 2])
                << decimal_to_binary(bytes[offset + 1])
                << decimal_to_binary(bytes[offset]);
            auto command = CommandParser::GetRiscvCommand(ss.str());

        }
    }
} // parser