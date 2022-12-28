cmake_minimum_required(VERSION 3.20)
project(AsmParser CXX)

set(CMAKE_CXX_STANDARD 20)



add_executable(AsmParser main.cpp parser/CommandParser.h utility/FileReader.h utility/FileWriter.h
        parser/CommandParser.cpp parser/SymtableParser.cpp parser/SymtableParser.h utility/SymtableElement.h
        parser/ElfParser.cpp parser/ElfParser.h utility/Section.h utility/Utility.h)

