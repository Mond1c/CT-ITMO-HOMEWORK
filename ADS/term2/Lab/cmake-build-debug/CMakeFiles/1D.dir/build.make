# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.25

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /home/mikhail/.local/share/JetBrains/Toolbox/apps/CLion/ch-0/231.6890.13/bin/cmake/linux/x64/bin/cmake

# The command to remove a file.
RM = /home/mikhail/.local/share/JetBrains/Toolbox/apps/CLion/ch-0/231.6890.13/bin/cmake/linux/x64/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/mikhail/Documents/Homework/ADS/term2/Lab

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/1D.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/1D.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/1D.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/1D.dir/flags.make

CMakeFiles/1D.dir/1D.cpp.o: CMakeFiles/1D.dir/flags.make
CMakeFiles/1D.dir/1D.cpp.o: /home/mikhail/Documents/Homework/ADS/term2/Lab/1D.cpp
CMakeFiles/1D.dir/1D.cpp.o: CMakeFiles/1D.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/1D.dir/1D.cpp.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/1D.dir/1D.cpp.o -MF CMakeFiles/1D.dir/1D.cpp.o.d -o CMakeFiles/1D.dir/1D.cpp.o -c /home/mikhail/Documents/Homework/ADS/term2/Lab/1D.cpp

CMakeFiles/1D.dir/1D.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/1D.dir/1D.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/mikhail/Documents/Homework/ADS/term2/Lab/1D.cpp > CMakeFiles/1D.dir/1D.cpp.i

CMakeFiles/1D.dir/1D.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/1D.dir/1D.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/mikhail/Documents/Homework/ADS/term2/Lab/1D.cpp -o CMakeFiles/1D.dir/1D.cpp.s

# Object files for target 1D
1D_OBJECTS = \
"CMakeFiles/1D.dir/1D.cpp.o"

# External object files for target 1D
1D_EXTERNAL_OBJECTS =

1D: CMakeFiles/1D.dir/1D.cpp.o
1D: CMakeFiles/1D.dir/build.make
1D: CMakeFiles/1D.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable 1D"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/1D.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/1D.dir/build: 1D
.PHONY : CMakeFiles/1D.dir/build

CMakeFiles/1D.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/1D.dir/cmake_clean.cmake
.PHONY : CMakeFiles/1D.dir/clean

CMakeFiles/1D.dir/depend:
	cd /home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/mikhail/Documents/Homework/ADS/term2/Lab /home/mikhail/Documents/Homework/ADS/term2/Lab /home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug /home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug /home/mikhail/Documents/Homework/ADS/term2/Lab/cmake-build-debug/CMakeFiles/1D.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/1D.dir/depend

