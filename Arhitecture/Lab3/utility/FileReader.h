#ifndef ASMPARSER_FILE_READER_H
#define ASMPARSER_FILE_READER_H

#include <iostream>
#include <memory>
#include <vector>
#include <fstream>
#include <cstdint>

namespace utility {
    class FileReader {
    private:
        std::ifstream file_;
    public:
        explicit FileReader(const std::string &fileName)
                : file_(fileName, std::ios_base::binary) {
            if (!file_.is_open()) {
                throw std::runtime_error("File with name \"" + fileName + "\" does not exist");
            }
            if (!fileName.ends_with(".elf")) {
                throw std::runtime_error("You can parse only *.elf files");
            }
        }

        FileReader(FileReader&) = delete;
        FileReader(FileReader&&) = delete;

        FileReader& operator=(FileReader&) = delete;
        FileReader& operator=(FileReader&&) = delete;

        ~FileReader() {
            file_.close();
        }

        std::vector<uint8_t> ReadBytes() {
            std::vector<uint8_t> bytes;
            while (file_) {
                bytes.push_back(file_.get());
            }
            return bytes;
        }
    };
}

#endif