cmake_minimum_required(VERSION 3.20)
project(rv3)

set(CMAKE_CXX_STANDARD 20)


add_executable(AsmParser main.cpp utility/FileReader.h utility/FileWriter.h parser/CommandParser.h parser/CommandParser.cpp
        utility/SymtableParser.cpp utility/SymtableParser.h utility/SymtableElement.h parser/ElfParser.h parser/ElfParser.cpp
        utility/Section.h utility/Utility.h)

