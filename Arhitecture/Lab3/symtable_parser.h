//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_SYMTABLE_PARSER_H
#define ASMPARSER_SYMTABLE_PARSER_H
#include <string>

class SymtableParser {
public:
    static std::string GetBind(int code);
    static std::string GetType(int code);
    static std::string GetVisibility(int code);
    static std::string GetId(int coder);
};


#endif //ASMPARSER_SYMTABLE_PARSER_H
