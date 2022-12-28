//
// Created by mike on 12/23/22.
//

#include "SymtableParser.h"
#include <stdexcept>


std::string utility::SymtableParser::GetBind(int code) {
    switch (code) {
        case 0:
            return "LOCAL";
        case 1:
            return "GLOBAL";
        case 2:
            return "WEAK";
        case 10:
            return "LOOS";
        case 12:
            return "HIOS";
        case 13:
            return "LOPROC";
        case 15:
            return "HIPROC";
    }
    throw std::invalid_argument(&"Unsupported symtable bind code = "[code]);
}

std::string utility::SymtableParser::GetType(int code) {
    switch (code) {
        case 0:
            return "NOTYPE";
        case 1:
            return "OBJECT";
        case 2:
            return "FUNC";
        case 3:
            return "SECTION";
        case 4:
            return "FILE";
        case 5:
            return "COMMON";
        case 6:
            return "TLS";
        case 10:
            return "LOOS";
        case 12:
            return "HIOS";
        case 13:
            return "LOPROC";
        case 15:
            return "HIPROC";
    }
    throw std::invalid_argument(&"Unsupported symtable type code = "[code]);
}

std::string utility::SymtableParser::GetVisibility(int code) {
    switch (code) {
        case 0:
            return "DEFAULT";
        case 1:
            return "INTERNAL";
        case 2:
            return "HIDDEN";
        case 3:
            return "PROTECTED";
    }
    throw std::invalid_argument(&"Unsupported symtable visibility code = "[code]);
}

std::string utility::SymtableParser::GetId(int code) {
    switch (code) {
        case 0:
            return "UNDEF";
        case 0xff00:
            return "LOPROC";
        case 0xff01:
            return "AFTER";
        case 0xff02:
            return "AMD64_LCOMMON";
        case 0xff1f:
            return "HIPROC";
        case 0xff20:
            return "LOOS";
        case 0xff3f:
            return "HIOS";
        case 0xfff1:
            return "ABS";
        case 0xfff2:
            return "COMMON";
        case 0xffff:
            return "XINDEX";
    }
    return std::to_string(code);
}
