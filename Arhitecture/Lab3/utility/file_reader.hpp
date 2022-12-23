#ifndef FILE_READER_H
#define FILE_READER_H
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
        explicit FileReader(const std::string& fileName)
            : file_(fileName, std::ios_base::binary) {
                if (!file_.is_open()) {
                    throw std::invalid_argument("File with name \"" + fileName + "\" does not exist");
                }
            }
        
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