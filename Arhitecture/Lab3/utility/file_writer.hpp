#ifndef FILE_WRITER_H
#define FILE_WRITER_H

#include <iostream>
#include <memory>
#include <vector>
#include <fstream>
#include <cstdint>

namespace utility {
    class FileWriter {
    private:
        std::ofstream file_;
    public:
        explicit FileWriter(const std::string& fileName)
            : file_(fileName) {}

        ~FileWriter() {
            file_.close();
        }

        void write(const std::string& str) {
            file_ << str;
        }

        void writeLine(const std::string& str) {
            file_ << str << std::endl;
        }
    };
}

#endif