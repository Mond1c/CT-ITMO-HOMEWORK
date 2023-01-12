cmake_minimum_required(VERSION 3.24)
project(omp4)

set(CMAKE_CXX_STANDARD 20)

OPTION (USE_OpenMP "Use OpenMP" ON)
IF(USE_OpenMP)
    FIND_PACKAGE(OpenMP)
    IF(OPENMP_FOUND)
        SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS} -march=native -O3")
    ENDIF()
ENDIF()

add_executable(omp4 hard.cpp utility.h histogram.h otsu.h)
