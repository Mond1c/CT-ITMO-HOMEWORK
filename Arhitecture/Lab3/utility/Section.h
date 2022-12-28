//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_SECTION_H
#define ASMPARSER_SECTION_H

#include <string>

namespace utility {
    struct Section {
        int name;
        int type;
        int flags;
        int addr;
        int offset;
        int size;
        int link;
        int info;
        int addrAlign;
        int entSize;

        Section(int name, int type, int flags, int addr, int offset, int size, int link, int info, int addrAlign,
                int entSize)
                : name(name), type(type), flags(flags), addr(addr), offset(offset), size(size), link(link), info(info),
                  addrAlign(addrAlign), entSize(entSize) {}

    };
}

#endif //ASMPARSER_SECTION_H
