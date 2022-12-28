#ifndef ASMPARSER_FILE_WRITER_H
#define ASMPARSER_FILE_WRITER_H

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
        explicit FileWriter(const std::string &fileName)
                : file_(fileName) {}

        ~FileWriter() {
            file_.close();
        }

        FileWriter(FileWriter&) = delete;
        FileWriter(FileWriter&&) = delete;

        FileWriter& operator=(FileWriter&) = delete;
        FileWriter& operator=(FileWriter&&) = delete;

        void write(const std::string &str) {
            file_ << str;
        }

        void writeLine(const std::string &str) {
            file_ << str << std::endl;
        }
    };
}

#endif