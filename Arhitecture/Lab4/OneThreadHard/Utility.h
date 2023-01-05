//
// Created by pocht on 05.01.2023.
//

#ifndef OMP4_UTILITY_H
#define OMP4_UTILITY_H
#include <string>
#include <memory>

namespace utility {
    template<typename... Args>
    static std::string GetFormatString(const std::string &format, Args... args) {
        std::size_t size = std::snprintf(nullptr, 0, format.c_str(), args ...) + 1;
        std::unique_ptr<char[]> buffer(new char[size]);
        std::snprintf(buffer.get(), size, format.c_str(), args ...);
        return {buffer.get(), buffer.get() + size - 1};
    }
}

#endif //OMP4_UTILITY_H
