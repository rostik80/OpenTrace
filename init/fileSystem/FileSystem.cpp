#include "FileSystem.h"
#include <filesystem>
#include <iostream>

namespace fs = std::filesystem;

void FileSystem::initialize() {
    // Створюємо всі необхідні директорії
    createDirectories({dataPath, logsPath, tmpPath, cachePath});
}

bool FileSystem::directoryExists(const std::string& path) {
    return fs::is_directory(path);
}

bool FileSystem::createDirectory(const std::string& path) {
    if (!directoryExists(path)) {
        try {
            fs::create_directories(path);
            return true;
        } catch (const fs::filesystem_error& e) {
            std::cerr << "[FileSystem::createDirectory] Error creating directory "
                      << path << ": " << e.what() << std::endl;
            return false;
        }
    }
    return true; // Директорія вже існує
}

void FileSystem::createDirectories(const std::vector<std::string>& paths) {
    for (const auto& path : paths) {
        createDirectory(path);
    }
}

void FileSystem::clearTemporary() {
    if (directoryExists("tmp")) {
        try {
            fs::remove_all("tmp");       // Видаляємо tmp і все всередині
            createDirectory("tmp");      // Відновлюємо tmp
            createDirectory("tmp/cache");// Відновлюємо cache
        } catch (const fs::filesystem_error& e) {
            std::cerr << "[FileSystem::clearTemporary] Error clearing tmp folder: "
                      << e.what() << std::endl;
        }
    }
}
