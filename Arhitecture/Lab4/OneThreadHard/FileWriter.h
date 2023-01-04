//
// Created by mond1c on 1/4/23.
//

#ifndef ONETHREADHARD_FILEWRITER_H
#define ONETHREADHARD_FILEWRITER_H
#include <string>
#include <fstream>
#include <memory>

namespace utility {
    class FileWriter {
    private:
        std::unique_ptr<std::ofstream> file_;
    public:
        explicit FileWriter(const std::string& fileName)
        : file_(std::make_unique<std::ofstream>(fileName)) {}

        ~FileWriter() {
            file_->close();
        }
    public:
        template<typename T>
        void Write(const T& str) {
            *file_ << str;
        }

        template<typename T>
        void WriteLine(const T& str) {
            *file_ << str << std::endl;
        }
    };
}

#endif //ONETHREADHARD_FILEWRITER_H
