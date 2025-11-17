cmake_minimum_required(VERSION 3.16)
project(OpenTrace)

set(CMAKE_CXX_STANDARD 17)

# -------------------------------
# Створюємо бібліотеку ядра (core)
# -------------------------------
add_library(OpenTraceCore
        init/InitializationManager.cpp
        init/fileSystem/FileSystem.cpp
        init/configManager/ConfigManager.cpp
)

target_include_directories(OpenTraceCore PUBLIC
        init
        init/fileSystem
        init/configManager
        external
)

# -------------------------------
# Основна програма
# -------------------------------
add_executable(OpenTrace main.cpp)

target_link_libraries(OpenTrace PRIVATE OpenTraceCore)

# -------------------------------
# Тести
# -------------------------------
add_executable(OpenTraceTests tests/main_test.cpp)

target_link_libraries(OpenTraceTests PRIVATE OpenTraceCore)
