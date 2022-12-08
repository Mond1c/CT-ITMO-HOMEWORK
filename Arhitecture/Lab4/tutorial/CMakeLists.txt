project(tutorial)

set(CMAKE_C_FLAGS ${CMAKE_C_FLAGS} -fopenmp)
add_executable(tutorial main.c)