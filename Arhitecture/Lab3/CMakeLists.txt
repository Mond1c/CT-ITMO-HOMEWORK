cmake_minimum_required(VERSION 3.24)
project(AsmParser CXX)

set(CMAKE_CXX_STANDARD 20)



add_executable(AsmParser main.cpp command_parser.h utility/file_reader.hpp utility/file_writer.hpp command_parser.cpp symtable_parser.cpp symtable_parser.h utility/riscv_command.h utility/symtable_element.h ElfParser.cpp ElfParser.h utility/Section.h)

