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
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/mike/Documents/Homework/Arhitecture/Lab3

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/mike/Documents/Homework/Arhitecture/Lab3/build

# Include any dependencies generated for this target.
include CMakeFiles/AsmParser.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/AsmParser.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/AsmParser.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/AsmParser.dir/flags.make

CMakeFiles/AsmParser.dir/src/main.cpp.o: CMakeFiles/AsmParser.dir/flags.make
CMakeFiles/AsmParser.dir/src/main.cpp.o: /home/mike/Documents/Homework/Arhitecture/Lab3/src/main.cpp
CMakeFiles/AsmParser.dir/src/main.cpp.o: CMakeFiles/AsmParser.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/mike/Documents/Homework/Arhitecture/Lab3/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/AsmParser.dir/src/main.cpp.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/AsmParser.dir/src/main.cpp.o -MF CMakeFiles/AsmParser.dir/src/main.cpp.o.d -o CMakeFiles/AsmParser.dir/src/main.cpp.o -c /home/mike/Documents/Homework/Arhitecture/Lab3/src/main.cpp

CMakeFiles/AsmParser.dir/src/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/AsmParser.dir/src/main.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/mike/Documents/Homework/Arhitecture/Lab3/src/main.cpp > CMakeFiles/AsmParser.dir/src/main.cpp.i

CMakeFiles/AsmParser.dir/src/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/AsmParser.dir/src/main.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/mike/Documents/Homework/Arhitecture/Lab3/src/main.cpp -o CMakeFiles/AsmParser.dir/src/main.cpp.s

# Object files for target AsmParser
AsmParser_OBJECTS = \
"CMakeFiles/AsmParser.dir/src/main.cpp.o"

# External object files for target AsmParser
AsmParser_EXTERNAL_OBJECTS =

AsmParser: CMakeFiles/AsmParser.dir/src/main.cpp.o
AsmParser: CMakeFiles/AsmParser.dir/build.make
AsmParser: CMakeFiles/AsmParser.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/mike/Documents/Homework/Arhitecture/Lab3/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable AsmParser"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/AsmParser.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/AsmParser.dir/build: AsmParser
.PHONY : CMakeFiles/AsmParser.dir/build

CMakeFiles/AsmParser.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/AsmParser.dir/cmake_clean.cmake
.PHONY : CMakeFiles/AsmParser.dir/clean

CMakeFiles/AsmParser.dir/depend:
	cd /home/mike/Documents/Homework/Arhitecture/Lab3/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/mike/Documents/Homework/Arhitecture/Lab3 /home/mike/Documents/Homework/Arhitecture/Lab3 /home/mike/Documents/Homework/Arhitecture/Lab3/build /home/mike/Documents/Homework/Arhitecture/Lab3/build /home/mike/Documents/Homework/Arhitecture/Lab3/build/CMakeFiles/AsmParser.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/AsmParser.dir/depend

