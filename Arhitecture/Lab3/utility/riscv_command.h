//
// Created by mike on 12/23/22.
//

#ifndef ASMPARSER_RISCV_COMMAND_H
#define ASMPARSER_RISCV_COMMAND_H

#include <string>

namespace utility {
    struct riscv_command {
        std::string instruction;
        std::string rs2;
        std::string rs1;
        std::string rd;
    };
}

#endif //ASMPARSER_RISCV_COMMAND_H
