# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.23

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
CMAKE_COMMAND = /home/mond1c/.local/share/JetBrains/Toolbox/apps/CLion/ch-0/222.4345.21/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /home/mond1c/.local/share/JetBrains/Toolbox/apps/CLion/ch-0/222.4345.21/bin/cmake/linux/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/mond1c/Documents/Homework/Arhitecture/Lab3

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/AsmParser.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/AsmParser.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/AsmParser.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/AsmParser.dir/flags.make

CMakeFiles/AsmParser.dir/main.cpp.o: CMakeFiles/AsmParser.dir/flags.make
CMakeFiles/AsmParser.dir/main.cpp.o: ../main.cpp
CMakeFiles/AsmParser.dir/main.cpp.o: CMakeFiles/AsmParser.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/AsmParser.dir/main.cpp.o"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/AsmParser.dir/main.cpp.o -MF CMakeFiles/AsmParser.dir/main.cpp.o.d -o CMakeFiles/AsmParser.dir/main.cpp.o -c /home/mond1c/Documents/Homework/Arhitecture/Lab3/main.cpp

CMakeFiles/AsmParser.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/AsmParser.dir/main.cpp.i"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/mond1c/Documents/Homework/Arhitecture/Lab3/main.cpp > CMakeFiles/AsmParser.dir/main.cpp.i

CMakeFiles/AsmParser.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/AsmParser.dir/main.cpp.s"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/mond1c/Documents/Homework/Arhitecture/Lab3/main.cpp -o CMakeFiles/AsmParser.dir/main.cpp.s

CMakeFiles/AsmParser.dir/command_parser.cpp.o: CMakeFiles/AsmParser.dir/flags.make
CMakeFiles/AsmParser.dir/command_parser.cpp.o: ../command_parser.cpp
CMakeFiles/AsmParser.dir/command_parser.cpp.o: CMakeFiles/AsmParser.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/AsmParser.dir/command_parser.cpp.o"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/AsmParser.dir/command_parser.cpp.o -MF CMakeFiles/AsmParser.dir/command_parser.cpp.o.d -o CMakeFiles/AsmParser.dir/command_parser.cpp.o -c /home/mond1c/Documents/Homework/Arhitecture/Lab3/command_parser.cpp

CMakeFiles/AsmParser.dir/command_parser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/AsmParser.dir/command_parser.cpp.i"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/mond1c/Documents/Homework/Arhitecture/Lab3/command_parser.cpp > CMakeFiles/AsmParser.dir/command_parser.cpp.i

CMakeFiles/AsmParser.dir/command_parser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/AsmParser.dir/command_parser.cpp.s"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/mond1c/Documents/Homework/Arhitecture/Lab3/command_parser.cpp -o CMakeFiles/AsmParser.dir/command_parser.cpp.s

CMakeFiles/AsmParser.dir/symtable_parser.cpp.o: CMakeFiles/AsmParser.dir/flags.make
CMakeFiles/AsmParser.dir/symtable_parser.cpp.o: ../symtable_parser.cpp
CMakeFiles/AsmParser.dir/symtable_parser.cpp.o: CMakeFiles/AsmParser.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building CXX object CMakeFiles/AsmParser.dir/symtable_parser.cpp.o"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/AsmParser.dir/symtable_parser.cpp.o -MF CMakeFiles/AsmParser.dir/symtable_parser.cpp.o.d -o CMakeFiles/AsmParser.dir/symtable_parser.cpp.o -c /home/mond1c/Documents/Homework/Arhitecture/Lab3/symtable_parser.cpp

CMakeFiles/AsmParser.dir/symtable_parser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/AsmParser.dir/symtable_parser.cpp.i"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/mond1c/Documents/Homework/Arhitecture/Lab3/symtable_parser.cpp > CMakeFiles/AsmParser.dir/symtable_parser.cpp.i

CMakeFiles/AsmParser.dir/symtable_parser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/AsmParser.dir/symtable_parser.cpp.s"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/mond1c/Documents/Homework/Arhitecture/Lab3/symtable_parser.cpp -o CMakeFiles/AsmParser.dir/symtable_parser.cpp.s

CMakeFiles/AsmParser.dir/ElfParser.cpp.o: CMakeFiles/AsmParser.dir/flags.make
CMakeFiles/AsmParser.dir/ElfParser.cpp.o: ../ElfParser.cpp
CMakeFiles/AsmParser.dir/ElfParser.cpp.o: CMakeFiles/AsmParser.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building CXX object CMakeFiles/AsmParser.dir/ElfParser.cpp.o"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/AsmParser.dir/ElfParser.cpp.o -MF CMakeFiles/AsmParser.dir/ElfParser.cpp.o.d -o CMakeFiles/AsmParser.dir/ElfParser.cpp.o -c /home/mond1c/Documents/Homework/Arhitecture/Lab3/ElfParser.cpp

CMakeFiles/AsmParser.dir/ElfParser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/AsmParser.dir/ElfParser.cpp.i"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/mond1c/Documents/Homework/Arhitecture/Lab3/ElfParser.cpp > CMakeFiles/AsmParser.dir/ElfParser.cpp.i

CMakeFiles/AsmParser.dir/ElfParser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/AsmParser.dir/ElfParser.cpp.s"
	/usr/lib64/ccache/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/mond1c/Documents/Homework/Arhitecture/Lab3/ElfParser.cpp -o CMakeFiles/AsmParser.dir/ElfParser.cpp.s

# Object files for target AsmParser
AsmParser_OBJECTS = \
"CMakeFiles/AsmParser.dir/main.cpp.o" \
"CMakeFiles/AsmParser.dir/command_parser.cpp.o" \
"CMakeFiles/AsmParser.dir/symtable_parser.cpp.o" \
"CMakeFiles/AsmParser.dir/ElfParser.cpp.o"

# External object files for target AsmParser
AsmParser_EXTERNAL_OBJECTS =

AsmParser: CMakeFiles/AsmParser.dir/main.cpp.o
AsmParser: CMakeFiles/AsmParser.dir/command_parser.cpp.o
AsmParser: CMakeFiles/AsmParser.dir/symtable_parser.cpp.o
AsmParser: CMakeFiles/AsmParser.dir/ElfParser.cpp.o
AsmParser: CMakeFiles/AsmParser.dir/build.make
AsmParser: CMakeFiles/AsmParser.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking CXX executable AsmParser"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/AsmParser.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/AsmParser.dir/build: AsmParser
.PHONY : CMakeFiles/AsmParser.dir/build

CMakeFiles/AsmParser.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/AsmParser.dir/cmake_clean.cmake
.PHONY : CMakeFiles/AsmParser.dir/clean

CMakeFiles/AsmParser.dir/depend:
	cd /home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/mond1c/Documents/Homework/Arhitecture/Lab3 /home/mond1c/Documents/Homework/Arhitecture/Lab3 /home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug /home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug /home/mond1c/Documents/Homework/Arhitecture/Lab3/cmake-build-debug/CMakeFiles/AsmParser.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/AsmParser.dir/depend

