//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_SYMTABLE_ELEMENT_H
#define ASMPARSER_SYMTABLE_ELEMENT_H

#include <string>
#include <memory>
#include "../symtable_parser.h"

namespace utility {
    struct symtable_element {
        std::string name;
        std::string type;
        std::string visibility;
        std::string id;
        std::string bind;

        int value = 0;
        int size = 0;
        int sym = 0;

        symtable_element(std::string name, int value, int size, int sym, int info, int other)
                : name(std::move(name)), value(value), size(size), sym(sym), type(SymtableParser::GetType(info & 0xf)),
                  visibility(SymtableParser::GetVisibility(other & 0x3)), id(SymtableParser::GetId(other >> 8)),
                  bind(SymtableParser::GetBind(info >> 4)) {}

        explicit symtable_element(std::string name) : name(std::move(name)), type("FUNC") {}

        std::string GetString() const {
            if (name.starts_with("LOC")) {
                return "";
            }
            char *buffer = new char[1024];
            sprintf(buffer, "[%4i] 0x%-15X %5i %-8s %-8s %-8s %6s %s\n", sym, value, size,
                    type.c_str(), bind.c_str(), visibility.c_str(), id.c_str(), name.c_str());
            return buffer;
        }
    };
}

#endif //ASMPARSER_SYMTABLE_ELEMENT_H
