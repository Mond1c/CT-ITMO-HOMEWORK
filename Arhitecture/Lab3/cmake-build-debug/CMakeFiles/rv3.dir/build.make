# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.24

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

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\cmake\win\bin\cmake.exe

# The command to remove a file.
RM = C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\cmake\win\bin\cmake.exe -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/rv3.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/rv3.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/rv3.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/rv3.dir/flags.make

CMakeFiles/rv3.dir/main.cpp.obj: CMakeFiles/rv3.dir/flags.make
CMakeFiles/rv3.dir/main.cpp.obj: C:/Users/pocht/Desktop/Homework/Arhitecture/Lab3/main.cpp
CMakeFiles/rv3.dir/main.cpp.obj: CMakeFiles/rv3.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/rv3.dir/main.cpp.obj"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/rv3.dir/main.cpp.obj -MF CMakeFiles\rv3.dir\main.cpp.obj.d -o CMakeFiles\rv3.dir\main.cpp.obj -c C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\main.cpp

CMakeFiles/rv3.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/rv3.dir/main.cpp.i"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\main.cpp > CMakeFiles\rv3.dir\main.cpp.i

CMakeFiles/rv3.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/rv3.dir/main.cpp.s"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\main.cpp -o CMakeFiles\rv3.dir\main.cpp.s

CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj: CMakeFiles/rv3.dir/flags.make
CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj: C:/Users/pocht/Desktop/Homework/Arhitecture/Lab3/parser/CommandParser.cpp
CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj: CMakeFiles/rv3.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj -MF CMakeFiles\rv3.dir\parser\CommandParser.cpp.obj.d -o CMakeFiles\rv3.dir\parser\CommandParser.cpp.obj -c C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\parser\CommandParser.cpp

CMakeFiles/rv3.dir/parser/CommandParser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/rv3.dir/parser/CommandParser.cpp.i"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\parser\CommandParser.cpp > CMakeFiles\rv3.dir\parser\CommandParser.cpp.i

CMakeFiles/rv3.dir/parser/CommandParser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/rv3.dir/parser/CommandParser.cpp.s"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\parser\CommandParser.cpp -o CMakeFiles\rv3.dir\parser\CommandParser.cpp.s

CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj: CMakeFiles/rv3.dir/flags.make
CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj: C:/Users/pocht/Desktop/Homework/Arhitecture/Lab3/utility/SymtableParser.cpp
CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj: CMakeFiles/rv3.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building CXX object CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj -MF CMakeFiles\rv3.dir\utility\SymtableParser.cpp.obj.d -o CMakeFiles\rv3.dir\utility\SymtableParser.cpp.obj -c C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\utility\SymtableParser.cpp

CMakeFiles/rv3.dir/utility/SymtableParser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/rv3.dir/utility/SymtableParser.cpp.i"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\utility\SymtableParser.cpp > CMakeFiles\rv3.dir\utility\SymtableParser.cpp.i

CMakeFiles/rv3.dir/utility/SymtableParser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/rv3.dir/utility/SymtableParser.cpp.s"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\utility\SymtableParser.cpp -o CMakeFiles\rv3.dir\utility\SymtableParser.cpp.s

CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj: CMakeFiles/rv3.dir/flags.make
CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj: C:/Users/pocht/Desktop/Homework/Arhitecture/Lab3/parser/ElfParser.cpp
CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj: CMakeFiles/rv3.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building CXX object CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj -MF CMakeFiles\rv3.dir\parser\ElfParser.cpp.obj.d -o CMakeFiles\rv3.dir\parser\ElfParser.cpp.obj -c C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\parser\ElfParser.cpp

CMakeFiles/rv3.dir/parser/ElfParser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/rv3.dir/parser/ElfParser.cpp.i"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\parser\ElfParser.cpp > CMakeFiles\rv3.dir\parser\ElfParser.cpp.i

CMakeFiles/rv3.dir/parser/ElfParser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/rv3.dir/parser/ElfParser.cpp.s"
	C:\Users\pocht\AppData\Local\JetBrains\Toolbox\apps\CLion\ch-0\223.8214.51\bin\mingw\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\parser\ElfParser.cpp -o CMakeFiles\rv3.dir\parser\ElfParser.cpp.s

# Object files for target rv3
rv3_OBJECTS = \
"CMakeFiles/rv3.dir/main.cpp.obj" \
"CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj" \
"CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj" \
"CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj"

# External object files for target rv3
rv3_EXTERNAL_OBJECTS =

rv3.exe: CMakeFiles/rv3.dir/main.cpp.obj
rv3.exe: CMakeFiles/rv3.dir/parser/CommandParser.cpp.obj
rv3.exe: CMakeFiles/rv3.dir/utility/SymtableParser.cpp.obj
rv3.exe: CMakeFiles/rv3.dir/parser/ElfParser.cpp.obj
rv3.exe: CMakeFiles/rv3.dir/build.make
rv3.exe: CMakeFiles/rv3.dir/linklibs.rsp
rv3.exe: CMakeFiles/rv3.dir/objects1.rsp
rv3.exe: CMakeFiles/rv3.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking CXX executable rv3.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\rv3.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/rv3.dir/build: rv3.exe
.PHONY : CMakeFiles/rv3.dir/build

CMakeFiles/rv3.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles\rv3.dir\cmake_clean.cmake
.PHONY : CMakeFiles/rv3.dir/clean

CMakeFiles/rv3.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3 C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3 C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug C:\Users\pocht\Desktop\Homework\Arhitecture\Lab3\cmake-build-debug\CMakeFiles\rv3.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/rv3.dir/depend

