cmake_minimum_required(VERSION 3.24)
project(OneThreadHard)

set(CMAKE_CXX_STANDARD 20)

add_executable(OneThreadHard hard.cpp Image.cpp Image.h Histogram.cpp Histogram.h Otsu.cpp Otsu.h FileWriter.h)
