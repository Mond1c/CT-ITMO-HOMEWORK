cmake_minimum_required(VERSION 3.24)
project(OneThreadHard)

set(CMAKE_CXX_STANDARD 20)

add_executable(OneThreadHard main.cpp Image.cpp Image.h Histogram.cpp Histogram.h)
