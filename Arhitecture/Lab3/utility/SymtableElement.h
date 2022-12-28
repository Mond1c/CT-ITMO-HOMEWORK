//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_SYMTABLEELEMENT_H
#define ASMPARSER_SYMTABLEELEMENT_H

#include <string>
#include <memory>
#include "../parser/SymtableParser.h"
#include "Utility.h"

namespace utility {
    struct SymtableElement {
        std::string name;
        std::string type;
        std::string visibility;
        std::string id;
        std::string bind;

        int value = 0;
        int size = 0;
        int sym = 0;

        SymtableElement(std::string name, int value, int size, int sym, int info, int other)
                : name(std::move(name)), value(value), size(size), sym(sym), type(SymtableParser::GetType(info & 0xf)),
                  visibility(SymtableParser::GetVisibility(other & 0x3)), id(SymtableParser::GetId(other >> 8)),
                  bind(SymtableParser::GetBind(info >> 4)) {}

        explicit SymtableElement(std::string name) : name(std::move(name)), type("FUNC") {}

        [[nodiscard]] std::string GetString() const {
            if (name.starts_with("LOC")) {
                return "";
            }
            return GetFormatString("[%4i] 0x%-15X %5i %-8s %-8s %-8s %6s %s\n", sym, value, size,
                                   type.c_str(), bind.c_str(), visibility.c_str(), id.c_str(), name.c_str());
        }
    };
}

#endif //ASMPARSER_SYMTABLEELEMENT_H